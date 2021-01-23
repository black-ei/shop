package com.wangyi.shop.dto;

import com.wangyi.shop.base.BaseDTO;
import com.wangyi.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "规格组数据传输DTO")
public class SpecGroupDTO extends BaseDTO {

    @NotNull(message = "主键不能为空",groups = {MingruiOperation.update.class})
    @ApiModelProperty(value = "主键",example = "1")
    private Integer id;

    @ApiModelProperty(value = "商品分类id,一个分类下有多个规格组",example = "1")
    @NotNull(message = "类型id不能为空",groups = {MingruiOperation.add.class,MingruiOperation.get.class})
    private Integer cid;

    @ApiModelProperty(value = "规格组的名称")
    @NotBlank(message = "规格组名称不能为空",groups = {MingruiOperation.add.class})
    private String name;
}
