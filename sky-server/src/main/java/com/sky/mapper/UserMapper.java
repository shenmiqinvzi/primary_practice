package com.sky.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.User;



@Mapper
public interface UserMapper {
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    @Insert("insert into user(openid,name,phone,sex,id_number,avatar,status,create_time,update_time)"+
            "values(#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    void insert(User user);

    @Select("select * from user where id=#{id}")
    User getById(Long id);
} 
