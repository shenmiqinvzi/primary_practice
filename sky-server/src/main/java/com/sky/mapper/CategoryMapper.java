package com.sky.mapper;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> pageQuery(CategoryPageQueryDTO dto);

    void insert(Category category);

    void update(Category category);

    @Select("select * from category where id = #{id}")
    Category getById(Long id);

    @Select("select * from category where status = 1 and type = #{type} order by sort asc")
    List<Category> listByType(Integer type);

    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);
}
