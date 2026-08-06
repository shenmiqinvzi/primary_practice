package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 员工表的数据库操作接口
 * 只需声明方法名，SQL 写在同名 XML 文件里（resources/mapper/EmployeeMapper.xml）
 */
@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     */
    Employee getByUsername(String username);

    List<Employee> pageQuery(String name);

    void insert(Employee employee);

    void update(Employee employee);

    void getById(Long id);
}
