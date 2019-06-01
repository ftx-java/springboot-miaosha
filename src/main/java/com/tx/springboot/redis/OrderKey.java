package com.tx.springboot.redis;

/**
 * UserKey
 *
 * @author tx
 * @date 2019/04/13
 */
public class OrderKey extends BasePrefix {

    private OrderKey( String prefix) {
        super( prefix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");

}
