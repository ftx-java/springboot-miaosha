package com.tx.springboot.controller;

import com.tx.springboot.config.ServerResponse;
import com.tx.springboot.pojo.vo.LoginVo;
import com.tx.springboot.redis.RedisService;
import com.tx.springboot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登录页面
 * http://localhost:8083/login/to_login
 *
 * @author tx
 * @Valid参数校验 JSR303参数校验 -->ISMobileValidator
 * @date 2019/04/13
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    /**
     * 服务器上QPS 69  并发：1000*10
     *
     * @return
     */
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public ServerResponse doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //登录
        String token = userService.login(response, loginVo);
        return ServerResponse.createSuccessServerResonpnseWithData(token);
    }

    @RequestMapping("/hello/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "tx");
        return "hello";
    }

}
