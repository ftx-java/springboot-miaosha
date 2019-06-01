package com.tx.springboot.config;

/**
 * 图书管理系统  前后端交互数据包装
 * 复用的后端返回对象
 * 200：表示成功
 * 500：表示错误，错误信息在msg字段中
 *
 * @author tx
 * @date 2019/03/27
 */
public enum ServerResponseCode {
    SUCCESS(0, "OK"),
    ERROR(500, "ERROR");
    private Integer code;
    private String msg;

    ServerResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
