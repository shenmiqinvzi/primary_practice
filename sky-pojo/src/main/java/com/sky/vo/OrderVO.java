package com.sky.vo;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单明细列表（详情接口需要） */
    private List<OrderDetail> orderDetailList;

    /** 订单菜品信息（字符串，用于列表展示，如：宫保鸡丁 x 2，鱼香肉丝 x 1） */
    private String orderDishes;
}