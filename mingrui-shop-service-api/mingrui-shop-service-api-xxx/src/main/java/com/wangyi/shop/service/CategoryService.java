package com.wangyi.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.entity.CategoryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    Result<JSONObject> edit(@Validated() @RequestBody CategoryEntity categoryEntity);
}
