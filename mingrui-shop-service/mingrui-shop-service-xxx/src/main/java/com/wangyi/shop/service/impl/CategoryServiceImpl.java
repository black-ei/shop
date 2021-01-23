package com.wangyi.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wangyi.shop.base.BaseApiService;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.entity.CategoryBrandEntity;
import com.wangyi.shop.entity.CategoryEntity;
import com.wangyi.shop.entity.SpecGroupEntity;
import com.wangyi.shop.mapper.CategoryBrandMapper;
import com.wangyi.shop.mapper.CategoryMapper;
import com.wangyi.shop.mapper.SpecGroupMapper;
import com.wangyi.shop.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CategoryBrandMapper categoryBrandMapper;

    @Resource
    private SpecGroupMapper specGroupMapper;

    @Override
    public Result<List<CategoryEntity>> selectCategoryByBrandId(@NotNull(message = "品牌id不能为空") Integer brandId) {
        return this.setResultSuccess(categoryMapper.selectCategoryByBrandId(brandId));
    }

    @Override
    @Transactional
    public Result<JSONObject> del(@NotNull(message = "id不能为空") Integer id) {
        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);
        if (null==categoryEntity) return this.setResultError("数据不存在!");
        //如果当前节点为父节点则不能删除
        if(categoryEntity.getIsParent()==1) return this.setResultError("当前节点为父节点,不能删除");

        //判断分类下是否有绑定的品牌!
        Example example1 = new Example(CategoryBrandEntity.class);
        example1.createCriteria().andEqualTo("categoryId",id);
        int i = categoryBrandMapper.selectCountByExample(example1);
        if (i>0) return this.setResultError("该分类下有绑定的品牌!,请先取消绑定");

        Example example = new Example(CategoryEntity.class);
        example.createCriteria().andEqualTo("parentId",categoryEntity.getParentId());
        //通过父id查询父节点下是否只有当前节点一个节点
        int count = categoryMapper.selectCountByExample(example);
        if(count<=1){
            //如果只有 当前节点一个节点就将父级节点修改为子节点
            CategoryEntity categoryEntity1 = new CategoryEntity();
            categoryEntity1.setId(categoryEntity.getParentId());
            categoryEntity1.setIsParent(0);
            categoryMapper.updateByPrimaryKeySelective(categoryEntity1);
        }

        //有两个以上直接删除
        categoryMapper.deleteByPrimaryKey(id);
        //删除分类下的规格组
        Example example2 = new Example(SpecGroupEntity.class);
        example2.createCriteria().andEqualTo("cid",id);
        specGroupMapper.deleteByExample(example2);

        return this.setResultSuccess("删除成功");
    }

    @Override
    @Transactional
    public Result<JSONObject> save(CategoryEntity categoryEntity) {
        //将父级节点isparent改为1
        CategoryEntity entity = new CategoryEntity();
        entity.setIsParent(1);
        entity.setId(categoryEntity.getParentId());
        categoryMapper.updateByPrimaryKeySelective(entity);

        //新增
        categoryEntity.setIsParent(0);
        categoryMapper.insertSelective(categoryEntity);
        return this.setResultSuccess("新增成功");
    }

    @Override
    @Transactional
    public Result<JSONObject> edit(CategoryEntity categoryEntity) {
        categoryMapper.updateByPrimaryKeySelective(categoryEntity);
        return this.setResultSuccess("修改成功");
    }

    @Override
    public Result<List<CategoryEntity>> list(@NotNull(message = "id不能为空") Integer pid) {
       CategoryEntity categoryEntity = new CategoryEntity();
       categoryEntity.setParentId(pid);
        return this.setResultSuccess(categoryMapper.select(categoryEntity));
    }
}
