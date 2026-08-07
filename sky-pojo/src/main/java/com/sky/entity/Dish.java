package com.sky.entity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 图片URL
     */
    private String image;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 状态：1起售 0停售
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 更新人
     */
    private Long updateUser;
}