package com.tx.springboot.service;

import com.tx.springboot.pojo.User;

import java.util.List;

/**
 * Demo class
 *
 * @author tx
 * @date 2018/10/29
 */
public interface UserService {
    public User select(int id);

    public void insert(User user);

    public void delete(int id);

    public void update(User user);

    public User findByName(String name);

    public User findById(int id);

   // public List<User> selectAll();
}
