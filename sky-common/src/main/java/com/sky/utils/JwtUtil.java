package com.sky.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 令牌工具类
 * generateToken: 登录成功后给用户"发令牌"（签名）
 * parseJWT    : 后续每次请求，验签并解析出用户信息
 */
public class JwtUtil {

    /**
     * 生成 JWT
     * @param secretKey 密钥（配置文件里指定）
     * @param ttlMillis 有效期（毫秒）
     * @param claims    要放入令牌里的数据，如 {"empId": 1}
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + ttlMillis))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     * 解析 JWT，返回载荷里的数据
     * @param token     前端传来的令牌
     * @param secretKey 密钥（必须与生成时一致）
     */
    public static Claims parseJWT(String secretKey, String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }
}
