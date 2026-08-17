package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 查询当前用户的所有地址
     */
    @Select("select * from address_book where user_id = #{userId} order by is_default desc, id desc")
    List<AddressBook> listByUserId(Long userId);

    /**
     * 根据ID查询地址
     */
    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);

    /**
     * 查询当前用户的默认地址
     */
    @Select("select * from address_book where user_id = #{userId} and is_default = 1")
    AddressBook getDefaultByUserId(Long userId);

    /**
     * 新增地址
     */
    @Insert("insert into address_book(user_id, consignee, sex, phone, province_code, province_name, " +
            "city_code, city_name, district_code, district_name, detail, label, is_default) " +
            "values(#{userId}, #{consignee}, #{sex}, #{phone}, #{provinceCode}, #{provinceName}, " +
            "#{cityCode}, #{cityName}, #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(AddressBook addressBook);

    /**
     * 修改地址
     */
    @Update("update address_book set consignee = #{consignee}, sex = #{sex}, phone = #{phone}, " +
            "province_code = #{provinceCode}, province_name = #{provinceName}, " +
            "city_code = #{cityCode}, city_name = #{cityName}, " +
            "district_code = #{districtCode}, district_name = #{districtName}, " +
            "detail = #{detail}, label = #{label}, is_default = #{isDefault} " +
            "where id = #{id}")
    void update(AddressBook addressBook);

    /**
     * 将当前用户所有地址的 is_default 置为 0
     */
    @Update("update address_book set is_default = 0 where user_id = #{userId}")
    void clearDefaultByUserId(Long userId);

    /**
     * 将指定地址设为默认（is_default = 1）
     */
    @Update("update address_book set is_default = 1 where id = #{id}")
    void setDefaultById(Long id);

    /**
     * 删除地址
     */
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);
}