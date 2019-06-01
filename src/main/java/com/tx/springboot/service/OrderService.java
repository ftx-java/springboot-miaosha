package com.tx.springboot.service;

import com.tx.springboot.pojo.MiaoshaOrder;
import com.tx.springboot.pojo.OrderInfo;
import com.tx.springboot.pojo.User;
import com.tx.springboot.pojo.vo.GoodsVo;

/**
 * 订单 service
 *
 * @author tx
 * @date 2019/04/17
 */
public interface OrderService {


    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);

    OrderInfo creatOrder(User user, GoodsVo goods);

    OrderInfo getOrderById(long orderId);

    /**
     * 重置秒杀订单 用户订单
     */
    void deleteOrders();
}
