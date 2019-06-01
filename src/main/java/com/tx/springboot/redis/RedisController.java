package com.tx.springboot.redis;

import com.tx.springboot.config.ServerResponse;
import com.tx.springboot.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RedisController
 *
 * @author tx
 * @date 2019/04/13
 */
@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    RedisService redisService;

    @RequestMapping("/redisSet")
    public ServerResponse redisSet() {
        User user = new User();
        user.setId(1L);
        user.setNickname("tx");
        Boolean res = redisService.set(UserKey.getById, "" + 1, user);
        return ServerResponse.createSuccessServerResonpnseWithData(res);
    }

    @RequestMapping("/redisGet")
    public ServerResponse redisGet() {
        User user = redisService.get(UserKey.getById, "" + 1, User.class);
        return ServerResponse.createSuccessServerResonpnseWithData(user);
    }
}
