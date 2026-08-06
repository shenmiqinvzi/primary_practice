package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜品及套餐分类实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键ID
    private Long id;

    // 类型：1 菜品分类，2 套餐分类
    private Integer type;

    // 分类名称（数据库有唯一约束，不能重名）
    private String name;

    // 排序序号（数字越小越靠前）
    private Integer sort;

    // 状态：1 启用，0 禁用
    private Integer status;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;

    // 创建人ID（对应 employee 表的 id）
    private Long createUser;

    // 更新人ID（对应 employee 表的 id）
    private Long updateUser;
}