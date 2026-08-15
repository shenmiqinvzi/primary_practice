package com.sky.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenUserInterceptor implements HandlerInterceptor{
    private final JwtProperties jwtProperties;

    public JwtTokenUserInterceptor(JwtProperties jwtProperties){
        this.jwtProperties=jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler){
        String token=request.getHeader(jwtProperties.getUserTokenName());
        log.info("用户端请求拦截，token：{}", token);

        try{
            Claims claims=JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId=Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户ID：{}", userId);
            BaseContext.setCurrentId(userId);
            return true;
        }catch(Exception e){
            log.error("用户端 JWT 解析失败，token：{}", token, e);
            // 解析失败，返回 401 未授权
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
