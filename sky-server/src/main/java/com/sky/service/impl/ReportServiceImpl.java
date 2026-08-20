package com.sky.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.sky.dto.GoodsSalesDTO;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.GoodsSalesExcelVO;
import com.sky.vo.OrderReportVO;
import com.sky.vo.OrderStatisticsExcelVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    private List<LocalDate> getDateRange(LocalDate begin,LocalDate end){
        List<LocalDate>dates=new ArrayList<>();
        LocalDate current=begin;
        while(!current.isAfter(end)){
            dates.add(current);
            current=current.plusDays(1);
        }
        return dates;
    }

    private LocalDateTime toStartOfDay(LocalDate date){
        return date.atStartOfDay();
    }

    private LocalDateTime toEndOfDay(LocalDate date){
        return date.atTime(LocalTime.MAX);
    }

    private Long getLongFromMap(Map<String,Object>map,String key){
        Object val=map.get(key);
        if(val==null) return 0L;
        if(val instanceof Long) return (Long) val;
        if(val instanceof Integer) return ((Integer) val).longValue();
        return Long.valueOf(val.toString());
    }

    private BigDecimal getBigDecimalFromMap(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val == null) return BigDecimal.ZERO;
        if (val instanceof BigDecimal) return (BigDecimal) val;
        return new BigDecimal(val.toString());
    }




    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin,LocalDate end){
        List<LocalDate>dateList=getDateRange(begin, end);
        LocalDateTime start=toStartOfDay(begin);
        LocalDateTime finish=toEndOfDay(end);

        List<Map<String,Object>>dbList=orderMapper.getTurnoverByDate(start, finish);
        Map<LocalDate,BigDecimal>turnoverMap=dbList.stream()
                                             .collect(Collectors.toMap(
                                                item->(LocalDate) item.get("date"),
                                                item->getBigDecimalFromMap(item, "amount")
                                             ));
        List<Double> turnoverList=new ArrayList<>();
        for(LocalDate date:dateList){
            BigDecimal amount=turnoverMap.getOrDefault(date, BigDecimal.ZERO);
            turnoverList.add(amount.doubleValue());
        }

        List<String>dateStrList=dateList.stream().map(LocalDate::toString).collect(Collectors.toList());
        return TurnoverReportVO.builder()
                                .dateList(dateStrList)
                                .turnoverList(turnoverList)
                                .build();
    }


    @Override
    public UserReportVO getUserStatistics(LocalDate begin,LocalDate end){
        List<LocalDate>dateList=getDateRange(begin, end);
        LocalDateTime start=toStartOfDay(begin);
        LocalDateTime finish=toEndOfDay(end);
        List<Map<String,Object>>dbList=orderMapper.getNewUserByDate(start, finish);
        Map<LocalDate,Long>newUserMap=dbList.stream()
                                      .collect(Collectors.toMap(
                                        item->(LocalDate) item.get("date"),
                                        item->getLongFromMap(item, "count")));
        List<Long>newUserList=new ArrayList<>();
        List<Long>totalUserList=new ArrayList<>();
        long cumulative=0;

        LocalDateTime beforeBegin=toStartOfDay(begin).minusSeconds(1);
        cumulative=userMapper.getTotalUserUntil(beforeBegin);
        for(LocalDate date:dateList){
            long newCount=newUserMap.getOrDefault(date, 0L);
            cumulative+=newCount;
            newUserList.add(newCount);
            totalUserList.add(cumulative);
        }
        List<String>dateStrList=dateList.stream()
                                .map(LocalDate::toString)
                                .collect(Collectors.toList());

        return UserReportVO.builder()
                .dateList(dateStrList)
                .newUserList(newUserList)
                .totalUserList(totalUserList)
                .build();
    }

    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateRange(begin, end);
        LocalDateTime start = toStartOfDay(begin);
        LocalDateTime finish = toEndOfDay(end);

        // 1. 查数据库：按天订单总数
        List<Map<String, Object>> orderDb = orderMapper.getOrderCountByDate(start, finish);
        Map<LocalDate, Integer> orderCountMap = orderDb.stream()
                .collect(Collectors.toMap(
                        item -> (LocalDate) item.get("date"),
                        item -> ((Long) item.get("count")).intValue()
                ));

        // 2. 查数据库：按天有效订单数
        List<Map<String, Object>> validDb = orderMapper.getValidOrderCountByDate(start, finish);
        Map<LocalDate, Integer> validOrderCountMap = validDb.stream()
                .collect(Collectors.toMap(
                        item -> (LocalDate) item.get("date"),
                        item -> ((Long) item.get("count")).intValue()
                ));

        // 3. 逐日补 0
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        int totalOrderCount = 0;
        int validOrderCount = 0;

        for (LocalDate date : dateList) {
            int orderCnt = orderCountMap.getOrDefault(date, 0);
            int validCnt = validOrderCountMap.getOrDefault(date, 0);

            orderCountList.add(orderCnt);
            validOrderCountList.add(validCnt);
            totalOrderCount += orderCnt;
            validOrderCount += validCnt;
        }

        // 4. 计算完成率
        double completionRate = 0.0;
        if (totalOrderCount > 0) {
            completionRate = (double) validOrderCount / totalOrderCount;
            BigDecimal rate = new BigDecimal(completionRate)
                    .setScale(1, BigDecimal.ROUND_HALF_UP);
            completionRate = rate.doubleValue();
        }

        List<String> dateStrList = dateList.stream()
                .map(LocalDate::toString)
                .collect(Collectors.toList());

        return OrderReportVO.builder()
                .dateList(dateStrList)
                .orderCountList(orderCountList)
                .validOrderCountList(validOrderCountList)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(completionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin,LocalDate end){
        LocalDateTime start = toStartOfDay(begin);
        LocalDateTime finish = toEndOfDay(end);

        List<GoodsSalesDTO> top10List = orderMapper.getSalesTop10(start, finish);
        List<String> nameList = top10List.stream()
                .map(GoodsSalesDTO::getName)
                .collect(Collectors.toList());

        List<Integer> numberList = top10List.stream()
                .map(GoodsSalesDTO::getNumber)
                .collect(Collectors.toList());

        while (nameList.size() < 10) {
            nameList.add("");
            numberList.add(0);
        }
        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

    @Override
    public void exportExcel(HttpServletResponse response) {
        try {
            // 1. 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");

            String fileName = URLEncoder.encode("订单报表_" + LocalDate.now(), "UTF-8")
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + fileName + ".xlsx");

            // 2. 准备数据（最近 30 天）
            LocalDate end = LocalDate.now().minusDays(1);  // 截止到昨天
            LocalDate begin = end.minusDays(29);           // 前 30 天

            // 3. 查询各项数据
            TurnoverReportVO turnover = getTurnoverStatistics(begin, end);
            UserReportVO user = getUserStatistics(begin, end);
            OrderReportVO order = getOrderStatistics(begin, end);
            SalesTop10ReportVO top10 = getSalesTop10(begin, end);

            // 4. 组装 Sheet1：订单概况
            List<OrderStatisticsExcelVO> sheet1List = new ArrayList<>();
            List<String> dateList = order.getDateList();
            for (int i = 0; i < dateList.size(); i++) {
                OrderStatisticsExcelVO row = OrderStatisticsExcelVO.builder()
                        .date(LocalDate.parse(dateList.get(i)))
                        .orderCount(order.getOrderCountList().get(i))
                        .validOrderCount(order.getValidOrderCountList().get(i))
                        .turnover(turnover.getTurnoverList().get(i))
                        .newUserCount(user.getNewUserList().get(i))
                        .totalUserCount(user.getTotalUserList().get(i))
                        .build();
                sheet1List.add(row);
            }

            // 5. 组装 Sheet2：销量 Top10
            List<GoodsSalesExcelVO> sheet2List = new ArrayList<>();
            for (int i = 0; i < top10.getNameList().size(); i++) {
                GoodsSalesExcelVO row = GoodsSalesExcelVO.builder()
                        .name(top10.getNameList().get(i))
                        .number(top10.getNumberList().get(i))
                        .build();
                sheet2List.add(row);
            }

            // 6. 写入 Excel
            EasyExcel.write(response.getOutputStream())
                    .sheet("订单概况")
                    .doWrite(sheet1List);

            EasyExcel.write(response.getOutputStream())
                    .sheet("销量Top10")
                    .doWrite(sheet2List);

            log.info("Excel 报表导出成功，日期范围：{} ~ {}", begin, end);

        } catch (IOException e) {
            log.error("Excel 导出失败", e);
            throw new RuntimeException("Excel 导出失败", e);
        }
    }
}
