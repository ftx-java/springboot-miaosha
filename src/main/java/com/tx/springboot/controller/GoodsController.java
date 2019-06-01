package com.tx.springboot.controller;

import com.tx.springboot.config.ServerResponse;
import com.tx.springboot.pojo.User;
import com.tx.springboot.pojo.vo.GoodsDetailVo;
import com.tx.springboot.pojo.vo.GoodsVo;
import com.tx.springboot.redis.GoodsKey;
import com.tx.springboot.redis.RedisService;
import com.tx.springboot.service.GoodsService;
import com.tx.springboot.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 商品
 * http://localhost:8083/goods/to_list
 * 对所有controller的方法中从客户端传过来的参数实体类User通过userArgumentResolver进行处理 实现cookie验证
 * 上述cookie验证可改为拦截器实现
 * <p>
 * 对商品列表页做的是页面缓存 对商品详情页做的是url缓存 其实二者没有本质的差别 适用于变化不大的场景
 *
 * @author tx
 * @date 2019/04/15
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response,
                       Model model, User user) {
        model.addAttribute("user", user);
        //去缓存中查看是否渲染过，有则直接拿到 么有的话就进行渲染 渲染后存到缓存中
        String html = redisService.get(GoodsKey.getGoodList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        //return "goods_list";
        //手动渲染
        WebContext context = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodList, "", html);
        }
        return html;
    }

    /**
     * 对商品详情页做了页面缓存
     */
    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response,
                          Model model, User user,
                          @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);
        //去缓存中查看是否渲染过，有则直接拿到 么有的话就进行渲染 渲染后存到缓存中
        String html = redisService.get(GoodsKey.getGoodDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);
        //秒杀时间状态
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;

        if (now < startAt) {
            //秒杀未开始 倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            //秒杀结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        //return "goods_detail";
        //手动渲染
        WebContext context = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", context);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodDetail, "" + goodsId, html);
        }
        return html;
    }

    /**
     * 对商品详情页做页面静态化（前后端分离）
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public ServerResponse detail(HttpServletRequest request, HttpServletResponse response, User user,
                                 @PathVariable("goodsId") long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        //秒杀还没开始，倒计时
        if (now < startAt) {
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            //秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return ServerResponse.createSuccessServerResonpnseWithData(vo);
    }

}
