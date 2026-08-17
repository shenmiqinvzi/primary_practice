package com.sky.constant;

/**
 * 订单状态常量（状态机）：
 * 待付款(1) → 待接单(2) → 已接单(3) → 派送中(4) → 已完成(5)
 * 另有 已取消(6)、已拒单(7)
 */
public class OrderStatusConstant {

    public static final Integer PENDING_PAYMENT = 1;      // 待付款
    public static final Integer TO_BE_CONFIRMED = 2;      // 待接单
    public static final Integer CONFIRMED = 3;            // 已接单
    public static final Integer DELIVERY_IN_PROGRESS = 4; // 派送中
    public static final Integer COMPLETED = 5;            // 已完成
    public static final Integer CANCELLED = 6;            // 已取消
    public static final Integer REFUND = 7;               // 已拒单/退款
}