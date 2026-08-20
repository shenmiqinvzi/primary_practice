package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrderPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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


/**
 * 批量取消超时未支付订单（条件更新，天然幂等）
 */
        @Update("UPDATE orders SET status = 6, cancel_reason = '超时未支付', cancel_time = #{cancelTime} " +
                "WHERE status = 1 AND order_time < #{thresholdTime}")
        int cancelTimeoutOrders(@Param("thresholdTime") LocalDateTime thresholdTime,
                                  @Param("cancelTime") LocalDateTime cancelTime);

/**
 * 批量完成派送中的订单（条件更新）
 */
        @Update("UPDATE orders SET status = 5, delivery_status = 3, delivery_time = #{deliveryTime} " +
                "WHERE status = 4")
        int completeDeliveryOrders(@Param("deliveryTime") LocalDateTime deliveryTime);

        /**
     * 按天统计营业额（已完成订单）
     */
        @Select("SELECT DATE(order_time) as date, SUM(amount) as amount " +
            "FROM orders " +
            "WHERE status = 5 AND order_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY DATE(order_time)")
        List<Map<String, Object>> getTurnoverByDate(@Param("begin") LocalDateTime begin,
                                                @Param("end") LocalDateTime end);

        

        

          @Select("SELECT DATE(order_time) as date, COUNT(id) as count " +
            "FROM orders " +
            "WHERE order_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY DATE(order_time)")
          List<Map<String, Object>> getOrderCountByDate(@Param("begin") LocalDateTime begin,
                                                  @Param("end") LocalDateTime end);

        @Select("select date(order_time) as date, count(id) as count"+
                "from orders where status=5 and order_time between #{begin} and #{end} group by date(order_time)")
         List<Map<String, Object>> getValidOrderCountByDate(@Param("begin") LocalDateTime begin,
                                                       @Param("end") LocalDateTime end);

        @Select("SELECT COUNT(id) FROM orders WHERE order_time BETWEEN #{begin} AND #{end}")
        Integer getTotalOrderCount(@Param("begin") LocalDateTime begin,
                               @Param("end") LocalDateTime end);


        @Select("SELECT COUNT(id) FROM orders WHERE status = 5 AND order_time BETWEEN #{begin} AND #{end}")
        Integer getValidOrderCount(@Param("begin") LocalDateTime begin,
                               @Param("end") LocalDateTime end);

        @Select("SELECT od.name, SUM(od.number) as number " +
            "FROM order_detail od " +
            "INNER JOIN orders o ON od.order_id = o.id " +
            "WHERE o.status = 5 AND o.order_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY od.name " +
            "ORDER BY number DESC " +
            "LIMIT 10")
        List<GoodsSalesDTO> getSalesTop10(@Param("begin") LocalDateTime begin,
                                          @Param("end") LocalDateTime end);

        @Select("select date(create_time) as date,count(id) as count"+
                "from user where create_time between #{begin} and #{end}"+
                "group by date(create_time)")
    List<Map<String,Object>> getNewUserByDate(@Param("begin") LocalDateTime begin,@Param("end") LocalDateTime end);

        List<Orders> pageQueryByUser(OrderPageQueryDTO dto);
}
