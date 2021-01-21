package com.wangyi.shop.base;

import com.wangyi.shop.validate.group.MingruiOperation;
import com.wangyi.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "BaseDTO用于数据传输,其他dto需要继承此类")
public class BaseDTO {

    @ApiModelProperty(value = "当前页", example = "1")
    private Integer page;

    @ApiModelProperty(value = "每页显示多少条",example = "5")
    private Integer rows;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "是否升序")
    private String order;

    @ApiModelProperty(hidden = true)
    public String getOrderByClause(){
        if(null!=sort && !"".equals(sort))return sort + " " + order.replace("false","asc").replace("true","desc");
        return "";
    }

}
