package com.wangyi.shop.service;

import com.wangyi.shop.base.Result;
import com.wangyi.shop.dto.SpuDTO;
import com.wangyi.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Api(tags = "商品接口")
@Validated
public interface GoodsService {

    @GetMapping(value = "goods/getSpuInfo")
    @ApiOperation(value = "查询商品集合")
    Result<List<SpuDTO>> spuInfo(SpuDTO spuDTO);
}
