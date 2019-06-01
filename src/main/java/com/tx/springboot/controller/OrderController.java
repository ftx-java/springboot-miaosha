package com.tx.springboot.controller;

import com.tx.springboot.config.ServerResponse;
import com.tx.springboot.pojo.OrderInfo;
import com.tx.springboot.pojo.User;
import com.tx.springboot.pojo.vo.GoodsVo;
import com.tx.springboot.pojo.vo.OrderDetailVo;
import com.tx.springboot.redis.RedisService;
import com.tx.springboot.service.GoodsService;
import com.tx.springboot.service.OrderService;
import com.tx.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 订单
 * 对所有controller的方法中从客户端传过来的参数实体类User通过userArgumentResolver进行处理 实现cookie验证
 *
 * @author tx
 * @date 2019/04/24
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public ServerResponse info( User user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return ServerResponse.createErrorServerResponseWithMsg("用户不存在");
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return ServerResponse.createErrorServerResponseWithMsg("订单不存在");
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return ServerResponse.createSuccessServerResonpnseWithData(vo);
    }

}
