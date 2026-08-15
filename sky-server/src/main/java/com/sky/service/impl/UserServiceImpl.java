package com.sky.service.impl;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.StatusConstant;
import com.sky.entity.User;
import com.sky.exception.BaseException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public UserLoginVO wxLogin(String code) {
        // ========== 模拟登录：直接把 code 当作 openid 使用 ==========
        // 任务书要求：不真实调用微信接口，直接拿前端传过来的 code 作为 openid
        // 这样在开发测试阶段无需配置 appid/secret，也不会因为网络问题导致失败
        String openid = code;
        log.info("模拟微信登录，openid：{}", openid);

        // 1. 根据 openid 查询用户
        User user = userMapper.getByOpenid(openid);

        // 2. 如果用户不存在，则自动注册（新用户）
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setStatus(StatusConstant.ENABLE);
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            userMapper.insert(user);
            log.info("新用户注册，openid：{}，id：{}", openid, user.getId());
        }

        // 3. 生成 JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims
        );

        // 4. 组装返回结果
        return UserLoginVO.builder()
                .id(user.getId())
                .openid(openid)
                .name(user.getName())
                .avatar(user.getAvatar())
                .token(token)
                .build();
    }

    @Override
    public User getById(Long id) {
        return userMapper.getById(id);
    }
}