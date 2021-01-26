package com.wangyi.shop.mapper;

import com.wangyi.shop.base.Result;
import com.wangyi.shop.entity.CategoryEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<CategoryEntity>, SelectByIdListMapper<CategoryEntity,Integer> {

    @Select("SELECT c.id,c.name FROM tb_brand b,tb_category_brand cb,tb_category c WHERE b.`id` = cb.`brand_id` AND cb.`category_id` =c.`id` AND b.`id` = #{id}")
    List<CategoryEntity> selectCategoryByBrandId(Integer id);
}
