package com.tx.springboot.config;


import lombok.extern.slf4j.Slf4j;

/**
 * 图书管理系统  前后端交互数据包装
 * 复用的后端返回对象
 * 200：表示成功
 * 500：表示错误，错误信息在msg字段中
 *
 * @author tx
 * @date 2019/03/27
 */
@Slf4j
public class ServerResponse {
    private String msg;
    private Integer code;
    private Object data;

    private ServerResponse(String msg, Integer code, Object data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    private ServerResponse(Object date) {
        this.data = date;
    }

    public static ServerResponse createSuccessWithData(Object data) {
        return new ServerResponse(data);
    }

    public static ServerResponse createSuccessServerResonpnseWithData(Object data) {
        return new ServerResponse(ServerResponseCode.SUCCESS.getMsg(),
                ServerResponseCode.SUCCESS.getCode(), data);
    }

    public static ServerResponse createSuccessServerResponseWithMsg(String msg) {
        return new ServerResponse(msg, ServerResponseCode.SUCCESS.getCode(), null);
    }

    public static ServerResponse createSuccessServerResonpnseWithMsgAndData(String msg, Object data) {
        return new ServerResponse(msg, ServerResponseCode.SUCCESS.getCode(), data);
    }

    public static ServerResponse createErrorServerResonpnseWithData(Object data) {
        return new ServerResponse(ServerResponseCode.ERROR.getMsg(),
                ServerResponseCode.ERROR.getCode(), data);
    }

    public static ServerResponse createErrorServerResponseWithMsg(String msg) {
        return new ServerResponse(msg, ServerResponseCode.ERROR.getCode(), null);
    }

    public static ServerResponse createErrorServerResonpnseWithMsgAndData(String msg, Object data) {
        return new ServerResponse(msg, ServerResponseCode.ERROR.getCode(), data);
    }

    public static boolean isSuccess(ServerResponse response) {
        return ServerResponseCode.SUCCESS.equals(response.getCode());
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
