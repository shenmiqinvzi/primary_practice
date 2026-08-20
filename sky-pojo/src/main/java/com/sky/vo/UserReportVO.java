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
public class UserReportVO implements Serializable {

    private List<String> dateList;       // 日期列表
    private List<Long> totalUserList;    // 累计用户数列表
    private List<Long> newUserList;      // 新增用户数列表
}