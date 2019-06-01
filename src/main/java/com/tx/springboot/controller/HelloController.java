package com.tx.springboot.controller;

import com.tx.springboot.rabbitmq.MqSender;
import com.tx.springboot.config.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Demo class
 *
 * @author tx
 * @date 2018/04/13
 */
@RestController
@RequestMapping("/demo")
public class HelloController {

    @Autowired
    MqSender sender;

    @RequestMapping("/hello")
    public ServerResponse hello() {
        return ServerResponse.createSuccessServerResonpnseWithData("hello");
    }

//    /**
//     * 测试mq 四种工作模式
//     * http://localhost:8083/demo/mq
//     */
//    @RequestMapping("/mq")
//    public ServerResponse mq() {
//        sender.send("你好 mq");
//        return ServerResponse.createSuccessServerResonpnseWithData("hello");
//    }
//
//    @RequestMapping("/mq/topic")
//    public ServerResponse topic() {
//        sender.sendTopic("你好 mq");
//        return ServerResponse.createSuccessServerResonpnseWithData("hello");
//    }
//
//    @RequestMapping("/mq/fanout")
//    public ServerResponse fanout() {
//        sender.sendFanout("你好 mq");
//        return ServerResponse.createSuccessServerResonpnseWithData("hello");
//    }
//
//    @RequestMapping("/mq/header")
//    public ServerResponse header() {
//        sender.sendHeader("你好 mq");
//        return ServerResponse.createSuccessServerResonpnseWithData("hello");
//    }

}
