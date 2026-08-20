package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderReportVO implements Serializable {

    private List<String> dateList;              // 日期列表
    private List<Integer> orderCountList;       // 当天订单总数列表
    private List<Integer> validOrderCountList;  // 当天有效订单数列表
    private Integer totalOrderCount;            // 区间总订单数
    private Integer validOrderCount;            // 区间有效订单数
    private Double orderCompletionRate;         // 订单完成率
}