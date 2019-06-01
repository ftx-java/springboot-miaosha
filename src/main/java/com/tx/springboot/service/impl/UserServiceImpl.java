package com.tx.springboot.service.impl;

import com.tx.springboot.config.ServerResponse;
import com.tx.springboot.dao.UserMapper;
import com.tx.springboot.exception.GlobalException;
import com.tx.springboot.pojo.User;
import com.tx.springboot.pojo.vo.LoginVo;
import com.tx.springboot.redis.MiaoshaUserKey;
import com.tx.springboot.redis.RedisService;
import com.tx.springboot.service.UserService;
import com.tx.springboot.utils.Md5Util;
import com.tx.springboot.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户 Service
 *
 * @author tx
 * @date 2019/04/13
 */

@Service
public class UserServiceImpl implements UserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;

    @Override
    public User getById(Long id) {
        //取对象缓存
        User user = redisService.get(MiaoshaUserKey.getById, "" + id, User.class);
        if (user != null) {
            return user;
        }
        //若缓存中没有则去数据中查找
        user = userMapper.getById(id);
        if (user != null) {
            redisService.set(MiaoshaUserKey.getById, "" + id, user);
        }
        return user;
    }

    @Override
    public boolean updatePassword(String token, long id, String formPass) {
        //拿到要更改的用户
        User user = getById(id);
        if (user == null) {
            throw new GlobalException(ServerResponse.createErrorServerResponseWithMsg("手机号码不存在"));
        }
        //更新数据库
        //新new了一个newUser是因为只想更改对应的密码字段 而不用将所有的字段都进行更改 这样可一定程度上的提高运行速度
        User newUser = new User();
        newUser.setId(id);
        newUser.setPassword(Md5Util.formPassToDbPass(formPass, user.getSalt()));
        userMapper.updateByPrimaryKey(newUser);
        //处理redis缓存 将其中的原有用户信息也进行更新 供以后使用
        //为何改getById与token的缓存的两种方式不同？getById是数据库中数据 token原来就在redis中
        //同时删除token会导致重新登录
        redisService.delete(MiaoshaUserKey.getById, "" + id);
        user.setPassword(newUser.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user);
        return true;
    }

    @Override
    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(ServerResponse.createErrorServerResponseWithMsg("用户信息不可为空"));
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        User user = userMapper.getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(ServerResponse.createErrorServerResponseWithMsg("手机号不存在"));
        }
        //验证密码
        String dbPass = user.getPassword();
        String dbSalt = user.getSalt();
        String newPass = Md5Util.formPassToDbPass(formPass, dbSalt);
        if (!dbPass.equals(newPass)) {
            throw new GlobalException(ServerResponse.createErrorServerResponseWithMsg("密码错误"));
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    @Override
    public void addCookie(HttpServletResponse response, String token, User user) {
        //将用户信息以及其对应的key保存到redis中
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置cookie的有效期以及路径
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        //将cookie返回给客户端
        response.addCookie(cookie);
    }

    @Override
    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(MiaoshaUserKey.token, token, User.class);
        //延长有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

}
