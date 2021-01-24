package com.wangyi.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wangyi.shop.base.BaseApiService;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.dto.SpecGroupDTO;
import com.wangyi.shop.entity.SpecGroupEntity;
import com.wangyi.shop.entity.SpecParamEntity;
import com.wangyi.shop.mapper.SpecGroupMapper;
import com.wangyi.shop.mapper.SpecParamMapper;
import com.wangyi.shop.service.SpecGroupService;
import com.wangyi.shop.utils.CopyBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class SpecGroupServiceImpl extends BaseApiService implements SpecGroupService {

    @Resource
    private SpecGroupMapper specGroupMapper;

    @Resource
    private SpecParamMapper specParamMapper;

    @Override
    public Result<JSONObject> delSpecGroup(@NotNull Integer id) {

        Example example = new Example(SpecParamEntity.class);
        example.createCriteria().andEqualTo("groupId",id);
        final int i = specParamMapper.selectCountByExample(example);
        if(i>0) return this.setResultError("请先删除规格组下的参数");
        specGroupMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> editSpecGroup(SpecGroupDTO specGroupDTO) {

        specGroupMapper.updateByPrimaryKeySelective(CopyBean.copyProperties(specGroupDTO,SpecGroupEntity.class));
        return this.setResultSuccess("修改成功!");
    }

    @Override
    public Result<JSONObject> saveSpecGroup(SpecGroupDTO specGroupDTO) {

        specGroupMapper.insertSelective(CopyBean.copyProperties(specGroupDTO,SpecGroupEntity.class));
        return this.setResultSuccess("新增成功!");
    }

    @Override
    public Result<List<SpecGroupEntity>> querySpecGroupByCid(@NotNull(message = "分类id不能为空") Integer cid) {

        Example example = new Example(SpecGroupEntity.class);
        example.createCriteria().andEqualTo("cid",cid);

        return this.setResultSuccess(specGroupMapper.selectByExample(example));
    }
}
