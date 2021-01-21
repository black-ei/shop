package com.wangyi.shop.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

@Data
@Table(name = "tb_category_brand")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "分类品牌中间表")
public class CategoryBrandEntity {

    @ApiModelProperty(value = "分类ID")
    private Integer categoryId;
    @ApiModelProperty(value = "品牌ID")
    private Integer brandId;

}