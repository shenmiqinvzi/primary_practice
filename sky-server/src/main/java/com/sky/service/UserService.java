package com.sky.service;


import com.sky.entity.User;
import com.sky.vo.UserLoginVO;

public interface UserService {
    UserLoginVO wxLogin(String code);

    User getById(Long id);
    
}
