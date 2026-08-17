package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单明细
     */
    @Insert("<script>" +
            "insert into order_detail(order_id, dish_id, setmeal_id, dish_flavor, name, image, number, amount) values " +
            "<foreach collection='list' item='detail' separator=','>" +
            "(#{detail.orderId}, #{detail.dishId}, #{detail.setmealId}, #{detail.dishFlavor}, " +
            "#{detail.name}, #{detail.image}, #{detail.number}, #{detail.amount})" +
            "</foreach>" +
            "</script>")
    void insertBatch(List<OrderDetail> orderDetails);
}