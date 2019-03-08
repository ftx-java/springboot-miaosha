package com.tx.springboot.controller;

import com.tx.springboot.dao.EmpMapper;
import com.tx.springboot.dao.UserMapper;
import com.tx.springboot.pojo.Emp;
import com.tx.springboot.pojo.User;
import com.tx.springboot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Demo class
 *
 * @author tx
 * @PathVariable可以用来映射URL中的占位符到目标方法的参数中
 * @date 2018/10/29
 */
@Controller()
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/HelloWorld")
    @ResponseBody
    public String hello() {
        return "Hello World";
    }

    /**
     * 测试thymeleaf
     */
    @RequestMapping("/testThymeleaf")
    public String testThymeleaf(Model model) {
        //把数据存到model中
        model.addAttribute("name", "tx");
        //返回test.html
        return "test";
    }

    @RequestMapping("/add")
    public String add() {
        return "user/add.html";
    }

    @RequestMapping("/update")
    public String update() {
        return "user/update.html";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    /**
     * shiro登录逻辑
     */
    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public String login(String name, String password, Model model) {
        //1、获取Subject
        Subject subject = SecurityUtils.getSubject();

        //2、封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);

        //3、执行登录方法
        try {
            subject.login(token);
            //登陆成功
            return "test";
        } catch (UnknownAccountException e) {
            //登录失败用户名不存在
            model.addAttribute("msg", "用户名不存在");
            return "login";
        } catch (IncorrectCredentialsException e) {
            //登录失败密码错误
            model.addAttribute("msg", "密码错误");
            return "login";
        }

    }

    /**
     * 授权未通过界面
     */
    @RequestMapping("/noAuth")
    public String noAuth() {
        return "/noAuth";
    }

}
