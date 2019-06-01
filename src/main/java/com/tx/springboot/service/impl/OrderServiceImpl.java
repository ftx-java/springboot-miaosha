package com.tx.springboot.service.impl;

import com.tx.springboot.dao.MiaoshaOrderMapper;
import com.tx.springboot.dao.OrderInfoMapper;
import com.tx.springboot.pojo.MiaoshaOrder;
import com.tx.springboot.pojo.OrderInfo;
import com.tx.springboot.pojo.User;
import com.tx.springboot.pojo.vo.GoodsVo;
import com.tx.springboot.redis.OrderKey;
import com.tx.springboot.redis.RedisService;
import com.tx.springboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 订单 service
 *
 * @author tx
 * @date 2019/04/17
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    MiaoshaOrderMapper miaoshaOrderMapper;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    RedisService redisService;

    @Override
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        //return miaoshaOrderMapper.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid, "" + userId + "_" + goodsId, MiaoshaOrder.class);

    }

    @Override
    public OrderInfo getOrderById(long orderId) {
        return orderInfoMapper.selectByPrimaryKey(orderId);
    }

    @Override
    @Transactional
    public OrderInfo creatOrder(User user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel((byte) 1);
        orderInfo.setStatus((byte) 0);
        orderInfo.setUserId(user.getId());
        orderInfoMapper.insertOrder(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        miaoshaOrderMapper.insertMiaoshaOrder(miaoshaOrder);
        //将生成的秒杀订单放入redis缓存中 以便下次查去redis中 提高qps
        redisService.set(OrderKey.getMiaoshaOrderByUidGid, "" + user.getId() + "_" + goods.getId(), miaoshaOrder);
        return orderInfo;
    }

    @Override
    public void deleteOrders() {
        orderInfoMapper.deleteOrders();
        orderInfoMapper.deleteMiaoshaOrders();
    }
}
