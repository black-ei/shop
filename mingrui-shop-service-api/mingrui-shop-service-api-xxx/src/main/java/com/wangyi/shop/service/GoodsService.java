package com.wangyi.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.dto.SkuDTO;
import com.wangyi.shop.dto.SpuDTO;
import com.wangyi.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "商品接口")
@Validated
public interface GoodsService {

    @GetMapping(value = "goods/getSpuInfo")
    @ApiOperation(value = "查询商品集合")
    Result<List<SpuDTO>> spuInfo(SpuDTO spuDTO);

    @PostMapping(value = "goods/save")
    @ApiOperation(value = "新增商品")
    Result<JSONObject> save(@Validated({MingruiOperation.add.class}) @RequestBody SpuDTO spuDTO);

    @GetMapping(value = "goods/getSpuDetail")
    @ApiOperation(value = "通过spuId获取SpuDetail信息")
    Result<List<SpuDTO>> querySpuDetailBySpuId(@NotNull Integer spuId);

    @GetMapping(value = "goods/getSkus")
    @ApiOperation(value = "通过spuId获取(Sku和stock)集合")
    Result<List<SkuDTO>> getSkus(@NotNull Integer spuId);

    @PutMapping(value = "goods/save")
    @ApiOperation(value = "修改商品")
    Result<JSONObject> edit(@Validated({MingruiOperation.update.class}) @RequestBody SpuDTO spuDTO);

    @DeleteMapping("goods/del")
    @ApiOperation(value = "删除商品")
    Result<JSONObject> del(@NotNull(message = "spuId不能为空") Integer spuId);

    @GetMapping("goods/down")
    @ApiOperation(value = "商品上下架")
    Result<JSONObject> down(@NotNull Integer spuId);



}
