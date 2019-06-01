package com.tx.springboot.redis;

/**
 * GoodsKey
 *
 * @author tx
 * @date 2019/04/13
 */
public class GoodsKey extends BasePrefix {
    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodList = new GoodsKey(60, "gl");
    public static GoodsKey getGoodDetail = new GoodsKey(60, "gd");
    public static GoodsKey getMiaoshaGoodsStock = new GoodsKey(0, "gs");


}
