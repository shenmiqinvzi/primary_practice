package com.sky.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {
    private Long id;        // 编辑员工时需要，新增可为空
    private String username;
    private String name;
    private String phone;
    private String sex;
    private String idNumber;
}
