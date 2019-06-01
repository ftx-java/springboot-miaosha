package com.tx.springboot.access;


import com.tx.springboot.pojo.User;

/**
 * ThreadLocal当前线程绑定的 向里面放东西 是放到当前线程里面来 不存在冲突
 * 每个线程单独存一份数据
 *
 * @author tx
 * @date 2019/04/26
 */
public class UserContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }

}
