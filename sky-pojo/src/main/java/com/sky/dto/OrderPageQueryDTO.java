package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderPageQueryDTO implements Serializable {

    private Integer page;         // 页码
    private Integer pageSize;     // 每页条数

    private String number;        // 订单号
    private String phone;         // 手机号
    private Integer status;       // 订单状态
    private LocalDateTime beginTime; // 开始时间
    private LocalDateTime endTime;   // 结束时间
}