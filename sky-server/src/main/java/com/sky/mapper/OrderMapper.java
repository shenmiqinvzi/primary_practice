package com.sky.mapper;

import com.sky.dto.OrderPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    /**
     * 新增订单（回填主键 id）
     */
    @Insert("insert into orders(number, status, user_id, address_book_id, order_time, checkout_time, " +
            "pay_method, pay_status, amount, remark, phone, address, user_name, consignee, " +
            "cancel_reason, rejection_reason, cancel_time, estimated_delivery_time, " +
            "delivery_status, delivery_time, pack_amount, tableware_number, tableware_status) " +
            "values(#{number}, #{status}, #{userId}, #{addressBookId}, #{orderTime}, #{checkoutTime}, " +
            "#{payMethod}, #{payStatus}, #{amount}, #{remark}, #{phone}, #{address}, #{userName}, #{consignee}, " +
            "#{cancelReason}, #{rejectionReason}, #{cancelTime}, #{estimatedDeliveryTime}, " +
            "#{deliveryStatus}, #{deliveryTime}, #{packAmount}, #{tablewareNumber}, #{tablewareStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Orders orders);

    List<Orders> pageQuery(OrderPageQueryDTO dto);

    Orders getById(Long id);

    OrderStatisticsVO countByStatus();

    void reject(Orders orders);

    void cancel(Orders orders);

    void updateStatus(Orders update);

    /**
 * 根据订单号查询订单
 */
    @Select("SELECT * FROM orders WHERE number = #{orderNumber}")
    Orders getByNumber(String orderNumber);
}