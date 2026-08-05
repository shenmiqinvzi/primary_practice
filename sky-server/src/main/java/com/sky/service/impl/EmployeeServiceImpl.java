package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        // 1. 按用户名查库
        Employee employee = employeeMapper.getByUsername(username);
        if (employee == null) {
            throw new AccountNotFoundException("账号不存在");
        }

        // 2. 密码校验：前端传明文，MD5 加密后与库里密文比对
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5.equals(employee.getPassword())) {
            throw new PasswordErrorException("密码错误");
        }

        // 3. 账号状态校验
        if (employee.getStatus() == StatusConstant.DISABLE) {
            throw new AccountLockedException("账号被锁定");
        }

        return employee;
    }

    @Override
    public PageResult pageQuery(Integer page,Integer pageSize,String name){
        PageHelper.startPage(page,pageSize);
        Page<Employee> p=(Page<Employee>) employeeMapper.pageQuery(name);
        return new PageResult(p.getTotal(),p.getResult());
    }

    @Override
    public void save(EmployeeDTO employeeDTO){
        Employee employee=new Employee();
        Employee exist=employeeMapper.getByUsername(employeeDTO.getUsername());
        if(exist!=null) throw new AccountNotFoundException("该用户已存在");
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setStatus(StatusConstant.ENABLE);

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
    }

    @Override
    public void startOrStop(Integer status, Long id){
        Employee employee=Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    @Override
    public void update(EmployeeDTO employeeDTO){
        Employee employee=new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }
}
