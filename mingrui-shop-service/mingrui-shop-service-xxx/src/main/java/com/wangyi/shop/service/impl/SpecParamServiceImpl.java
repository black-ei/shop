package com.wangyi.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wangyi.shop.base.BaseApiService;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.dto.SpecParamDTO;
import com.wangyi.shop.entity.SpecParamEntity;
import com.wangyi.shop.mapper.SpecParamMapper;
import com.wangyi.shop.service.SpecParamService;
import com.wangyi.shop.utils.CopyBean;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class SpecParamServiceImpl extends BaseApiService implements SpecParamService {

    @Resource
    private SpecParamMapper specParamMapper;

    @Override
    public Result<JSONObject> del(@NotNull Integer id) {
        specParamMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess("删除成功");
    }

    @Override
    public Result<JSONObject> edit(SpecParamDTO specParamDTO) {
        specParamMapper.updateByPrimaryKeySelective(CopyBean.copyProperties(specParamDTO,SpecParamEntity.class));
        return this.setResultSuccess("修改成功");
    }

    @Override
    public Result<JSONObject> save(SpecParamDTO specParamDTO) {
        specParamMapper.insertSelective(CopyBean.copyProperties(specParamDTO,SpecParamEntity.class));
        return this.setResultSuccess("新增成功");
    }

    @Override
    public Result<List<SpecParamEntity>> getSpecParamInfo(SpecParamDTO specParamDTO) {
        Example example = new Example(SpecParamEntity.class);
        if(null!=specParamDTO){
            SpecParamEntity specParamEntity = CopyBean.copyProperties(specParamDTO, SpecParamEntity.class);
            if(null!=specParamEntity.getGroupId()) example.createCriteria().andEqualTo("groupId",specParamEntity.getGroupId());
            if (null!=specParamEntity.getCid()) example.createCriteria().andEqualTo("cid",specParamEntity.getCid());
        }
        return this.setResultSuccess(specParamMapper.selectByExample(example));
    }
}
