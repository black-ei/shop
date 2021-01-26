package com.wangyi.shop.mapper;

import com.wangyi.shop.entity.BrandEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<BrandEntity> {

    @Select("SELECT b.`id`,b.`name` FROM tb_category c,tb_category_brand cb,tb_brand b WHERE c.`id` = cb.`category_id` AND cb.`brand_id` = b.`id` AND c.`id` =#{cid}")
    List<BrandEntity> queryBrandBycategoryId(Integer cid);
}
