package com.tx.springboot.service.impl;

import com.tx.springboot.dao.UserMapper;
import com.tx.springboot.pojo.User;
import com.tx.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Demo class
 *
 * @author tx
 * @date 2018/10/29
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User select(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }

    @Override
    public void delete(int id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public User findByName(String name) {
        return userMapper.findByName(name);
    }

    @Override
    public User findById(int id) {
        return userMapper.findById(id);
    }

//    @Override
//    public List<User> selectAll() {
//
//        return userMapper.selectAll;
//    }
}
