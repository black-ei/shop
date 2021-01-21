package com.wangyi.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.entity.CategoryEntity;
import com.wangyi.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "商品分类接口")
@Validated
public interface CategoryService {

    @GetMapping(value = "category/list")
    @ApiOperation(value = "通过pid查询商品分类")
    Result<List<CategoryEntity>> list(@NotNull(message = "id不能为空") Integer pid);

    @PutMapping(value = "category/edit")
    @ApiOperation(value = "通过id修改商品分类")
    Result<JSONObject> edit(@Validated(value = {MingruiOperation.update.class}) @RequestBody CategoryEntity categoryEntity);

    @DeleteMapping(value = "category/del")
    @ApiOperation(value = "删除商品分类")
    Result<JSONObject> del(@NotNull(message = "id不能为空") Integer id);

    @PostMapping(value = "category/save")
    @ApiOperation(value = "新增商品分类")
    Result<JSONObject> save(@Validated(value = {MingruiOperation.add.class}) @RequestBody CategoryEntity categoryEntity);

    @GetMapping(value = "category/queryCategoryByBrandId")
    @ApiOperation(value = "通过brandID查询品牌绑定分类")
    Result<List<CategoryEntity>> selectCategoryByBrandId(@NotNull(message = "品牌id不能为空") Integer brandId);

}
