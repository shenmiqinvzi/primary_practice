package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderSubmitDTO implements Serializable {

    private Long addressBookId;      // 地址簿ID（必填）
    private Integer payMethod;       // 支付方式：1微信 2支付宝
    private String remark;           // 备注（选填）

    // ===== 餐具相关（前端可选传） =====
    private Integer tablewareNumber; // 餐具数量（默认 0）
    private Integer tablewareStatus; // 餐具状态：0不需要 1需要（默认 0）
}