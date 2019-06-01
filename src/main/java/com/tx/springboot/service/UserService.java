package com.tx.springboot.service;

import com.tx.springboot.pojo.User;
import com.tx.springboot.pojo.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 用户 Service
 *
 * @author tx
 * @date 2019/04/13
 */
public interface UserService {
    /**
     * 根据用户id查找用户信息
     *
     * @param id
     * @return
     */
    User getById(Long id);

    /**
     * 更改密码
     *
     * @param id
     * @param formPass
     * @return
     */
    boolean updatePassword(String token, long id, String formPass);

    /**
     * 用户登录
     *
     * @param response
     * @param loginVo
     * @return
     */
    String login(HttpServletResponse response, LoginVo loginVo);

    /**
     * 添加cookie
     *
     * @param response
     * @param user
     * @param token
     * @return
     */
    void addCookie(HttpServletResponse response, String token, User user);

    /**
     * 根据客户端发的token去找redis中之前登录时存放的用户信息
     *
     * @param response
     * @param token
     * @return
     */
    User getByToken(HttpServletResponse response, String token);
}
