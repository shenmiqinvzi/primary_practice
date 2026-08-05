package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取 application.yml 中 sky.jwt 开头的配置
 * Spring 启动时自动把配置值填充进下面的字段
 */
@Component
@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {

    /** 管理端员工令牌：签名密钥 */
    private String adminSecretKey;

    /** 管理端员工令牌：有效期（毫秒） */
    private long adminTtl;

    /** 管理端员工令牌：前端放令牌的请求头名称 */
    private String adminTokenName;

    /** 用户端顾客令牌：签名密钥 */
    private String userSecretKey;

    /** 用户端顾客令牌：有效期（毫秒） */
    private long userTtl;

    /** 用户端顾客令牌：请求头名称 */
    private String userTokenName;
}
