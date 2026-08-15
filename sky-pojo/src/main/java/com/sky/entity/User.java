package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String openid;         // 微信用户唯一标识（登录凭证）
    private String name;           // 用户姓名
    private String phone;          // 手机号
    private String sex;            // 性别：0-女 1-男
    private String idNumber;       // 身份证号
    private String avatar;         // 头像路径
    private Integer status;        // 状态：1-正常 0-禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}