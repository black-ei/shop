package com.wangyi.shop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangyi.shop.base.BaseApiService;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.dto.SpuDTO;
import com.wangyi.shop.entity.CategoryEntity;
import com.wangyi.shop.entity.SpuEntity;
import com.wangyi.shop.mapper.BrandMapper;
import com.wangyi.shop.mapper.CategoryMapper;
import com.wangyi.shop.mapper.SpuMapper;
import com.wangyi.shop.service.GoodsService;
import com.wangyi.shop.status.HTTPStatus;
import com.wangyi.shop.utils.CopyBean;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GoodsServiceImpl extends BaseApiService implements GoodsService {

    @Resource
    private SpuMapper spuMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private BrandMapper brandMapper;

    @Override
    public Result<List<SpuDTO>> spuInfo(SpuDTO spuDTO) {
        if (null!=spuDTO.getRows() && null!=spuDTO.getPage() && spuDTO.getRows()>0) PageHelper.startPage(spuDTO.getPage(),spuDTO.getRows());//分页

        Example example = new Example(SpuEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(null!=spuDTO){
            //copybean
            SpuEntity spuEntity = CopyBean.copyProperties(spuDTO, SpuEntity.class);
            //查询条件:上下架
            if(null!=spuDTO.getSaleable() && spuDTO.getSaleable()<2 && spuDTO.getSaleable()>=0) criteria.andEqualTo("saleable",spuEntity.getSaleable());
            //查询条件:商品标题title搜索
            if(null!=spuEntity.getTitle() && !"".equals(spuEntity.getTitle())) criteria.andLike("title","%"+spuEntity.getTitle()+"%");
            //排序
            if(null!=spuDTO.getOrder() && !"".equals(spuDTO.getOrder())) example.setOrderByClause(spuDTO.getOrderByClause());
        }
        List<SpuEntity> spuEntities = spuMapper.selectByExample(example);
        //封装spuDTO集合
        List<SpuDTO> spuDTOList  = spuEntities.stream().map(spu -> {
            SpuDTO copyspu = CopyBean.copyProperties(spu, SpuDTO.class);
            //查询分类name
            copyspu.setCategoryName(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())
                    .stream().map(cid -> {
                        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(cid);
                        if(null==categoryEntity) return "";
                        return categoryEntity.getName();
                    })
                    .collect(Collectors.joining("/")));
            //查询品牌名称
            copyspu.setBrandName(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
            return copyspu;
        }).collect(Collectors.toList());
        PageInfo<SpuEntity> spuEntityPageInfo = new PageInfo<>(spuEntities); //完成分页
        return this.setResult(HTTPStatus.OK,spuEntityPageInfo.getTotal()+"",spuDTOList);
    }
}
