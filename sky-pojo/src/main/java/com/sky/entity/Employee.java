package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体类，与数据库 employee 表字段一一对应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /** 姓名 */
    private String name;

    /** 用户名（登录账号） */
    private String username;

    /** 密码 */
    private String password;

    /** 手机号 */
    private String phone;

    /** 性别：1男 0女 */
    private String sex;

    /** 身份证号 */
    private String idNumber;

    /** 状态：1启用 0禁用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人 */
    private Long createUser;

    /** 修改人 */
    private Long updateUser;
}
