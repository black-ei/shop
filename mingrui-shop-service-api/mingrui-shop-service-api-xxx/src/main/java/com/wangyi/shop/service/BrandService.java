package com.wangyi.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.dto.BrandDTO;
import com.wangyi.shop.entity.BrandEntity;
import com.wangyi.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "品牌接口")
@Validated
public interface BrandService {

    @GetMapping("brand/list")
    @ApiOperation(value = "查询品牌信息")
    Result<PageInfo<BrandEntity>> page(BrandDTO brandDTO);

    @PutMapping(value = "brand/save")
    @ApiOperation(value = "根据id修改品牌信息")
    Result<JsonObject> edit(@RequestBody @Validated(value = {MingruiOperation.update.class}) BrandDTO brandDTO);

    @PostMapping(value = "brand/save")
    @ApiOperation(value = "新增品牌信息")
    Result<JsonObject> save(@RequestBody @Validated(value = {MingruiOperation.add.class}) BrandDTO brandDTO);

    @DeleteMapping(value = "brand/delete")
    @ApiOperation(value = "通过id删除品牌")
    Result<JSONObject> deleteByBrandId(@NotNull(message = "brandId不能为空") Integer id);
}
