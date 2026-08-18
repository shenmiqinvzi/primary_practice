package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisticsVO implements Serializable {

    private Integer toBeConfirmed;      // 待接单数量（status=2）
    private Integer confirmed;          // 已接单数量（status=3）
    private Integer deliveryInProgress; // 派送中数量（status=4）
}