package com.sky.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisticsExcelVO implements Serializable {

    @ExcelProperty("日期")
    private LocalDate date;

    @ExcelProperty("订单数")
    private Integer orderCount;

    @ExcelProperty("有效订单数")
    private Integer validOrderCount;

    @ExcelProperty("营业额")
    private Double turnover;

    @ExcelProperty("新增用户")
    private Long newUserCount;

    @ExcelProperty("累计用户")
    private Long totalUserCount;
}