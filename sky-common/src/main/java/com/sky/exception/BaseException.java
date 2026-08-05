package com.sky.exception;

/**
 * 业务异常基类
 * 所有自定义业务异常都继承它，全局异常处理器统一拦截
 */
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }
}
