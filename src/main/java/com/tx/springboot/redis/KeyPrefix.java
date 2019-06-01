package com.tx.springboot.redis;

/**
 * KeyPrefix
 *
 * @author tx
 * @date 2019/04/13
 */
public interface KeyPrefix {
    public int expireSeconds();

    public String getPrefix();
}
