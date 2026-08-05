package com.sky.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果类
 * 所有接口的返回值都封装成这个格式：{ code, msg, data }
 *  code = 1 表示成功，code = 0 表示失败
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg;   //错误信息
    private T data;       //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }
}
