package com.wangyi.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.wangyi.shop.base.BaseApiService;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.dto.BrandDTO;
import com.wangyi.shop.entity.BrandEntity;
import com.wangyi.shop.entity.CategoryBrandEntity;
import com.wangyi.shop.mapper.BrandMapper;
import com.wangyi.shop.mapper.CategoryBrandMapper;
import com.wangyi.shop.service.BrandService;
import com.wangyi.shop.utils.CopyBean;
import com.wangyi.shop.utils.PinyinUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryBrandMapper categoryBrandMapper;

    @Override
    public Result<List<BrandEntity>> queryBrandByCategoryId(@NotNull Integer cid) {
        return this.setResultSuccess(brandMapper.queryBrandBycategoryId(cid));
    }

    @Override
    @Transactional
    public Result<JSONObject> deleteByBrandId(@NotNull(message = "brandId不能为空") Integer id) {
        brandMapper.deleteByPrimaryKey(id);
        //删除中间表信息
        this.delCategoryBrandByBrandId(id);
        return this.setResultSuccess("删除成功");
    }

    @Override
    @Transactional
    public Result<JsonObject> save(BrandDTO brandDTO) {

        BrandEntity brandEntity = CopyBean.copyProperties(brandDTO, BrandEntity.class);
        //处理首字母
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName()),false).charAt(0));
        brandMapper.insertSelective(brandEntity);
        this.insertCategoryBrand(brandDTO.getCategories(),brandEntity.getId());
        return this.setResultSuccess("新增成功!");
    }

    @Override
    @Transactional
    public Result<JsonObject> edit(BrandDTO brandDTO) {
        BrandEntity brandEntity = CopyBean.copyProperties(brandDTO, BrandEntity.class);
        //品牌首字母处理
        brandEntity.setLetter(PinyinUtil.getUpperCase(brandEntity.getName().charAt(0)+"",false).charAt(0));
        System.err.println(brandEntity.getLetter());

        brandMapper.updateByPrimaryKeySelective(brandEntity);
        //修改品牌分类(处理关系表)
        //1.根据BrandId删除关系表所有信息
        this.delCategoryBrandByBrandId(brandEntity.getId());
        //2.重新新增关系
        this.insertCategoryBrand(brandDTO.getCategories(),brandEntity.getId());
        return this.setResultSuccess("修改成功");
    }

    @Override
    public Result<PageInfo<BrandEntity>> page(BrandDTO brandDTO) {

        //分页
       if(brandDTO.getRows()!=-1) PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());

        //copy
        BrandEntity brandEntity = CopyBean.copyProperties(brandDTO, BrandEntity.class);
        Example example = new Example(BrandEntity.class);
         //排序
        if(null!=brandDTO.getSort() && !"".equals(brandDTO.getSort())) example.setOrderByClause(brandDTO.getOrderByClause());
        //条件查询
        if(null!=brandDTO.getName() && !"".equals(brandDTO.getName())) example.createCriteria().andLike("name","%"+brandEntity.getName()+"%");
        List<BrandEntity> list = brandMapper.selectByExample(example);

        //会查询总条数,(共有多少页,起始页数)
        return this.setResultSuccess(new PageInfo<>(list));
    }

    //根据BrandId删除categoryBrand关系表关联数据
    private void delCategoryBrandByBrandId(Integer branId){
        Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",branId);
        categoryBrandMapper.deleteByExample(example);
    }

    //新增categoryBrand关系表信息
    private void insertCategoryBrand(String categoryIds,Integer brandId){
        if(categoryIds.contains(",")){
            //批量新增
            categoryBrandMapper.insertList(Arrays.asList(categoryIds.split(",")).stream().map(categoryId -> new CategoryBrandEntity(Integer.valueOf(categoryId),brandId)).collect(Collectors.toList()));
        }else{
            categoryBrandMapper.insert(new CategoryBrandEntity(Integer.valueOf(categoryIds),brandId));
        }
    }
}
