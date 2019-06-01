package com.tx.springboot.redis;

/**
 * GoodsKey
 *
 * @author tx
 * @date 2019/04/13
 */
public class AccessKey extends BasePrefix {
    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    //public static AccessKey access = new AccessKey(5, "access");

    public static AccessKey withExpire(int seconds) {
        return new AccessKey(seconds,"access");
    }
}
