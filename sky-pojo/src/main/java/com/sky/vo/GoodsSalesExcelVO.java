package com.sky.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSalesExcelVO implements Serializable {

    @ExcelProperty("菜品名称")
    private String name;

    @ExcelProperty("销量")
    private Integer number;
}