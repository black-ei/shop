package com.wangyi.shop.service.impl;

import com.wangyi.shop.base.BaseApiService;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.entity.CategoryEntity;
import com.wangyi.shop.mapper.CategoryMapper;
import com.wangyi.shop.service.CategoryService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Result<List<CategoryEntity>> list(@NotNull(message = "id不能为空") Integer pid) {
       CategoryEntity categoryEntity = new CategoryEntity();
       categoryEntity.setParentId(pid);
        return this.setResultSuccess(categoryMapper.select(categoryEntity));
    }
}
