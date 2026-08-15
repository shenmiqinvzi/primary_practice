package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {

    /**
     * 微信登录凭证（前端调用 wx.login 后拿到的 code）
     */
    private String code;
}