package com.tx.springboot.controller;

import com.tx.springboot.config.ServerResponse;
import com.tx.springboot.pojo.User;
import com.tx.springboot.redis.RedisService;
import com.tx.springboot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 用户
 * http://localhost:8083/user/info
 *
 * @author tx
 * @date 2019/04/21
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/info")
    @ResponseBody
    public ServerResponse info(User user) {
        return ServerResponse.createSuccessServerResonpnseWithData(user);
    }
}
