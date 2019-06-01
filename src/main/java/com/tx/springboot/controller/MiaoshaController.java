package com.tx.springboot.controller;

import com.tx.springboot.access.AccessLimit;
import com.tx.springboot.config.ServerResponse;
import com.tx.springboot.pojo.MiaoshaOrder;
import com.tx.springboot.pojo.OrderInfo;
import com.tx.springboot.pojo.User;
import com.tx.springboot.pojo.vo.GoodsVo;
import com.tx.springboot.rabbitmq.MiaoshaMessage;
import com.tx.springboot.rabbitmq.MqSender;
import com.tx.springboot.redis.*;
import com.tx.springboot.service.GoodsService;
import com.tx.springboot.service.MiaoshaService;
import com.tx.springboot.service.OrderService;
import com.tx.springboot.service.UserService;
import com.tx.springboot.utils.Md5Util;
import com.tx.springboot.utils.UUIDUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 实现秒杀功能
 *
 * @author tx
 * @date 2019/04/17
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    MqSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 重置redis与数据库  用于重新测试
     * http://localhost:8083/miaosha/reset
     *
     * @return
     */
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse reset() {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for (GoodsVo goods : goodsList) {
            goods.setStockCount(20);
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), 20);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getMiaoshaOrderByUidGid);
        redisService.delete(MiaoshaKey.isGoodsOver);
        miaoshaService.reset(goodsList);
        return ServerResponse.createSuccessServerResonpnseWithData(true);
    }

    /**
     * 系统初始化 将秒杀商品对应的库存刷到redis中
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
            //为商品做标记 flase 表示有库存可以秒杀 防止多余的用户都去访问redis
            localOverMap.put(goods.getId(), false);
        }
    }

    /**
     * 服务器上QPS 1306  并发：5000*10
     * 调优后 QPS 2115   并发：5000*10
     */
    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse miaosha(User user,
                                  @RequestParam("goodsId") long goodsId,
                                  @PathVariable("path") String path) {
        if (user == null) {
            return ServerResponse.createErrorServerResponseWithMsg("用户不存在");
        }

        //验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if (!check) {
            return ServerResponse.createErrorServerResponseWithMsg("非法请求");
        }

        //利用内存标记 减少redis访问 若库存不足之后所有用户跳过下面所有操作 直接返回失败
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return ServerResponse.createErrorServerResponseWithMsg("秒杀结束");
        }
        //判断库存 在redis上预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return ServerResponse.createErrorServerResponseWithMsg("库存不足");
        }
        //判断是否秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return ServerResponse.createErrorServerResponseWithMsg("不可以重复秒杀");
        }
        //符合上述条件可以秒杀  入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(mm);
        //返回0 代表排队中
        return ServerResponse.createSuccessServerResonpnseWithData(0);

        /*
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return ServerResponse.createErrorServerResponseWithMsg("库存不足");
        }
        //判断是否秒杀过了 将订单放入redis缓存中（对象缓存） 提高qps
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return ServerResponse.createErrorServerResponseWithMsg("不可以重复秒杀");
        }
        //进行秒杀 减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        return ServerResponse.createSuccessServerResonpnseWithData(orderInfo);
        */
    }

    /**
     * result:
     * orderId:成功
     * -1：失败
     * 0：排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse miaoshaResult(User user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return ServerResponse.createErrorServerResponseWithMsg("用户不存在");
        }
        long reult = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return ServerResponse.createSuccessServerResonpnseWithData(reult);
    }

    /**
     * 秒杀接口隐藏
     * 接口改造  带上PathVariable参数
     *
     * @AccessLimit(seconds=5, maxCount=5, needLogin=true)拦截器 5秒钟最多访问5次
     * 同时cookie验证可改为拦截器实现
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getMiaoshaPath(User user, @RequestParam("goodsId") long goodsId,
                                         @RequestParam("verifyCode") int verifyCode) {
        if (user == null) {
            return ServerResponse.createErrorServerResponseWithMsg("用户不存在");
        }

        //验证验证码
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return ServerResponse.createErrorServerResonpnseWithData("验证失败");
        }
        String path = miaoshaService.creatMiaoshaPath(user, goodsId);
        return ServerResponse.createSuccessServerResonpnseWithData(path);
    }

    /**
     * 图形化验证码
     */
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getMiaoshaVerifyCode(HttpServletResponse response, User user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return ServerResponse.createErrorServerResponseWithMsg("用户不存在");
        }
        try {
            BufferedImage image = miaoshaService.creatMiaoshaVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createErrorServerResonpnseWithData("验证失败");
        }
    }

}
