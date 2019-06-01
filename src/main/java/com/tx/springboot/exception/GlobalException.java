package com.tx.springboot.exception;

import com.tx.springboot.config.ServerResponse;

/**
 * 设置全局异常类型
 *
 * @author tx
 * @date 2019/04/15
 */

public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private ServerResponse sr;

    public GlobalException(ServerResponse sr) {
        super(sr.toString());
        this.sr = sr;
    }

    public ServerResponse getSr() {
        return sr;
    }
}
