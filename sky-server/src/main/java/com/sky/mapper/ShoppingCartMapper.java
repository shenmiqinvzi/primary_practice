package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sky.entity.ShoppingCart;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShoppingCartMapper {

    @Select("select * from shopping_cart where user_id=#{userid} order by create_time desc")
    List<ShoppingCart> listByUserId(Long userId);

    @Select("select * from shopping_cart where user_id = #{userId} and dish_id = #{dishId} and dish_flavor = #{dishFlavor}")
    ShoppingCart getByDishAndFlavor(@Param("userId") Long userId,
                                    @Param("dishId") Long dishId,
                                    @Param("dishFlavor") String dishFlavor);

    @Select("select * from shopping_cart where user_id = #{userId} and setmeal_id = #{setmealId}")
    ShoppingCart getBySetmeal(@Param("userId") Long userId,
                              @Param("setmealId") Long setmealId);

    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "values(#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number=#{number} where id =#{id}")
    void updateNumberById(@Param("id") Long id,@Param("number") Integer number);

    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

}
