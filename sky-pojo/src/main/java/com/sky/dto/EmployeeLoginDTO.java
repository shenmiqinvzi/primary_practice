package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工登录时，前端传来的数据（入参）
 */
@Data
public class EmployeeLoginDTO implements Serializable {

    private String username;

    private String password;
}
