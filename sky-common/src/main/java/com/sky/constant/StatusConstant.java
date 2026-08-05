package com.sky.constant;

/**
 * 状态常量：数据库里很多表用 1/0 表示启用/停用
 * 把魔法数字集中到常量类，避免到处写死 1 和 0
 */
public class StatusConstant {

    public static final Integer ENABLE = 1;   // 启用
    public static final Integer DISABLE = 0;  // 停用
}
