package com.tx.springboot.controller;

import com.tx.springboot.pojo.User;
import com.tx.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demo class
 *
 * @author tx
 * @date 2018/10/29
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/select/{id}")
    public User select(@PathVariable("id") int id){
        return userService.select(id);
    }

    @RequestMapping("/insert")
    public void insert(User user){
        userService.insert(user);
    }

    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable("id") int id){
        userService.delete(id);
    }

    @RequestMapping("/update")
    public void update(User user){
        userService.update(user);
    }
}
