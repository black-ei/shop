package com.wangyi.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.dto.SpecParamDTO;
import com.wangyi.shop.entity.SpecParamEntity;
import com.wangyi.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "规格组参数接口")
@Validated
public interface SpecParamService {

    @GetMapping(value = "specparam/getSpecParamInfo")
    @ApiOperation(value = "查询规格组下的参数")
    Result<List<SpecParamEntity>> getSpecParamInfo(SpecParamDTO specParamDTO);

    @PostMapping(value = "specparam/save")
    @ApiOperation(value = "新增规格组参数")
    Result<JSONObject> save(@Validated(value = {MingruiOperation.add.class}) @RequestBody SpecParamDTO specParamDTO);

    @PutMapping(value = "specparam/save")
    @ApiOperation(value = "新增规格组参数")
    Result<JSONObject> edit(@Validated(value = {MingruiOperation.update.class}) @RequestBody SpecParamDTO specParamDTO);

    @DeleteMapping(value = "specparam/del")
    @ApiOperation(value = "删除规格组参数")
    Result<JSONObject> del(@NotNull Integer id);
}
