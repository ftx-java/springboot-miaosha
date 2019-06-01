package com.tx.springboot.utils;

import java.util.UUID;

/**
 * 生成UUID
 *
 * @author tx
 * @date 2019/04/15
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
