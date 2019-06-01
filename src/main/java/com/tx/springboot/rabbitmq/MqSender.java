package com.tx.springboot.rabbitmq;

import com.tx.springboot.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * rabbitmq 消息发送者
 *
 * @author tx
 * @date 2019/04/24
 */
@Service
public class MqSender {
    private static Logger log = LoggerFactory.getLogger(MqSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage mm) {
        String msg = RedisService.beanToString(mm);
        log.info("send message" + msg);
        //采用Direct模式
        amqpTemplate.convertAndSend(MqConfig.MIAOSHA_QUEUE, msg);
    }

//    public void send(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send message" + msg);
//        amqpTemplate.convertAndSend(MqConfig.QUEUE, msg);
//    }
//
//    public void sendTopic(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send topic message:" + msg);
//        amqpTemplate.convertAndSend(MqConfig.TOPIC_EXCHANGE, "topic.key1", msg + "1");
//        amqpTemplate.convertAndSend(MqConfig.TOPIC_EXCHANGE, "topic.key2", msg + "2");
//    }
//
//    public void sendFanout(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send fanout message:" + msg);
//        amqpTemplate.convertAndSend(MqConfig.FANOUT_EXCHANGE, "", msg);
//    }
//
//    public void sendHeader(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send fanout message:" + msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("header1", "value1");
//        properties.setHeader("header2", "value2");
//        Message obj = new Message(msg.getBytes(), properties);
//        amqpTemplate.convertAndSend(MqConfig.HEADERS_EXCHANGE, "", obj);
//    }


}
