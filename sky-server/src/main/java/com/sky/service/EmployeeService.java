package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;

/**
 * 员工相关业务接口
 */
public interface EmployeeService {

    /**
     * 员工登录
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    PageResult pageQuery(Integer page, Integer pageSize, String name);

    void save(EmployeeDTO employeeDTO);

    void startOrStop(Integer status, Long id);

    void update(EmployeeDTO employeeDTO);
}
