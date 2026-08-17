package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String number;                 // 订单号（唯一）
    private Integer status;                // 订单状态：1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款
    private Long userId;                   // 用户ID
    private Long addressBookId;            // 地址簿ID
    private LocalDateTime orderTime;       // 下单时间
    private LocalDateTime checkoutTime;    // 结账时间
    private Integer payMethod;             // 支付方式：1微信 2支付宝
    private Integer payStatus;             // 支付状态：0未支付 1已支付 2退款
    private BigDecimal amount;             // 订单总金额
    private String remark;                 // 备注
    private String phone;                  // 手机号（冗余）
    private String address;                // 详细地址（冗余）
    private String userName;               // 用户姓名（冗余）
    private String consignee;              // 收货人（冗余）
    private String cancelReason;           // 取消原因
    private String rejectionReason;        // 拒单原因
    private LocalDateTime cancelTime;      // 取消时间
    private LocalDateTime estimatedDeliveryTime; // 预计送达时间
    private Integer deliveryStatus;        // 配送状态：1待配送 2配送中 3已送达
    private LocalDateTime deliveryTime;    // 送达时间
    private BigDecimal packAmount;         // 打包费
    private Integer tablewareNumber;       // 餐具数量
    private Integer tablewareStatus;       // 餐具状态：0不需要 1需要
}