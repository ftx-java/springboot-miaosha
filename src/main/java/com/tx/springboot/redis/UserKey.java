package com.tx.springboot.redis;
/**
 * UserKey
 *
 * @author tx
 * @date 2019/04/13
 */
public class UserKey extends BasePrefix {
    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
