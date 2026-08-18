package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersDTO implements Serializable {

    private Long id;
    private String rejectionReason;  // 拒单原因
    private String cancelReason;     // 取消原因
}