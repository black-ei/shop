package com.wangyi.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.dto.SpecGroupDTO;
import com.wangyi.shop.entity.SpecGroupEntity;
import com.wangyi.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;


@Api(tags = "规格组接口")
@Validated
public interface SpecGroupService {

    @GetMapping(value = "spec/groups")
    @ApiOperation(value = "通过分类id查询规格组")
    Result<List<SpecGroupEntity>> querySpecGroupByCid(@NotNull(message = "分类id不能为空") Integer cid);

    @PostMapping(value = "spec/save")
    @ApiOperation(value = "新增规格组")
    Result<JSONObject> saveSpecGroup(@Validated(value = {MingruiOperation.add.class}) @RequestBody SpecGroupDTO specGroupDTO);

    @PutMapping(value = "spec/save")
    @ApiOperation(value = "修改规格组")
    Result<JSONObject> editSpecGroup(@Validated(value = {MingruiOperation.update.class}) @RequestBody SpecGroupDTO specGroupDTO);

    @DeleteMapping(value = "spec/del")
    @ApiOperation(value = "通过分类id查询规格组")
    Result<JSONObject> delSpecGroup(@NotNull Integer id);

}
