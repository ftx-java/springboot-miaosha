package com.tx.springboot.rabbitmq;

import com.tx.springboot.pojo.MiaoshaOrder;
import com.tx.springboot.pojo.User;
import com.tx.springboot.pojo.vo.GoodsVo;
import com.tx.springboot.redis.RedisService;
import com.tx.springboot.service.GoodsService;
import com.tx.springboot.service.MiaoshaService;
import com.tx.springboot.service.OrderService;
import com.tx.springboot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * rabbitmq 消息接收者
 *
 * @author tx
 * @date 2019/04/24
 */
@Service
public class MqReceiver {

    private static Logger log = LoggerFactory.getLogger(MqReceiver.class);

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

    @RabbitListener(queues = MqConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message" + message);
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        User user = mm.getUser();
        long goodsId = mm.getGoodsId();

        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goods);
    }

//    @RabbitListener(queues = MqConfig.QUEUE)
//    public void receive(String message) {
//        log.info("receive message" + message);
//    }
//
//    @RabbitListener(queues = MqConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message) {
//        log.info(" topic  queue1 message:" + message);
//    }
//
//    @RabbitListener(queues = MqConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message) {
//        log.info(" topic  queue2 message:" + message);
//    }
//
//    @RabbitListener(queues = MqConfig.HEADER_QUEUE)
//    public void receiveHeaderQueue(byte[] message) {
//        log.info(" header  queue message:" + new String(message));
//    }
}
