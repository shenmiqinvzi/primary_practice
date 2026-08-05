package com.sky.handler;

import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * Service 层抛出的业务异常，由这里统一拦截，转成 { code:0, msg:错误信息 } 返回
 * 这样 Controller 不用写 try-catch，代码非常干净
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 拦截所有业务异常
     */
    @ExceptionHandler(BaseException.class)
    public Result<String> baseExceptionHandler(BaseException ex) {
        log.error("业务异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 兜底：拦截所有未预期的异常
     */
    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception ex) {
        log.error("系统异常：", ex);
        return Result.error("服务器内部错误，请稍后重试");
    }
}
