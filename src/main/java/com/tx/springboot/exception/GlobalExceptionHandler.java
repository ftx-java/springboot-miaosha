package com.tx.springboot.exception;

import com.tx.springboot.config.ServerResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 设置全局异常处理器
 * 用来处理所有GlobalException抛出的异常以及参数校验@Valid的异常以及其他的异常等等
 *
 * @author tx
 * @ExceptionHandler：统一处理某一类异常，从而能够减少代码重复率和复杂度
 * @ControllerAdvice：异常集中处理，更好的使业务逻辑与异常处理剥离开
 * @date 2019/04/15
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ServerResponse exceptionHandler(HttpServletResponse response, Exception e) {
        //打印异常
        e.printStackTrace();
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            ServerResponse errors = ex.getSr();
            String msg = errors.getMsg();
            return ServerResponse.createErrorServerResponseWithMsg(msg);
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return ServerResponse.createErrorServerResonpnseWithMsgAndData(msg, "参数绑定异常");
        } else {
            return ServerResponse.createErrorServerResponseWithMsg("服务异常");
        }
    }
}
