package com.wangyi.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangyi.shop.base.BaseApiService;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.dto.SkuDTO;
import com.wangyi.shop.dto.SpuDTO;
import com.wangyi.shop.entity.*;
import com.wangyi.shop.mapper.*;
import com.wangyi.shop.service.GoodsService;
import com.wangyi.shop.status.HTTPStatus;
import com.wangyi.shop.utils.CopyBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
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

    @Resource
    private SpuDetailMapper spuDetailMapper;

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private StockMapper stockMapper;

    @Override
    public Result<JSONObject> down(@NotNull Integer spuId) {
        SpuEntity spuEntity = spuMapper.selectByPrimaryKey(spuId);
        if (spuEntity==null) return this.setResultError("数据不存在");
        spuEntity.setSaleable(spuEntity.getSaleable()==1?0:1);
        spuMapper.updateByPrimaryKeySelective(spuEntity);
        return this.setResultSuccess("操作成功");
    }

    @Override
    @Transactional
    public Result<JSONObject> del(@NotNull(message = "spuId不能为空") Integer spuId) {
        SpuEntity spuEntity = spuMapper.selectByPrimaryKey(spuId);
        if(null==spuEntity) return this.setResultError("数据不存在!");
        spuMapper.deleteByPrimaryKey(spuId);//删除spu
        spuDetailMapper.deleteByPrimaryKey(spuEntity.getId());//删除spuDetail
        delSkuAndStock(spuEntity.getId());//删除Sku和Stock

        return this.setResultSuccess("删除成功");
    }

    @Override
    @Transactional
    public Result<JSONObject> edit(SpuDTO spuDTO) {
        SpuEntity spu = spuMapper.selectByPrimaryKey(spuDTO.getId());
        if (spu==null) return this.setResultError("数据不存在");
        final Date date = new Date();//统一时间
        //修改spu
        SpuEntity spuEntity = CopyBean.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setLastUpdateTime(date);
        spuMapper.updateByPrimaryKeySelective(spuEntity);
        //修改spuDetail
        spuDetailMapper.updateByPrimaryKeySelective(CopyBean.copyProperties(spuDTO.getSpuDetail(), SpuDetailEntity.class));
        //修改sku 先删除sku和stock 再新增sku和stock
        delSkuAndStock(spuEntity.getId());//删除sku和stock
        insertSkuAndStock(spuDTO,spuEntity.getId(),date);//新增sku和stock
        return this.setResultSuccess("修改成功!");
    }

    @Override
    public Result<List<SkuDTO>> getSkus(@NotNull Integer spuId) {
        return this.setResultSuccess(skuMapper.querySkuAndStockList(spuId));
    }

    @Override
    public Result<List<SpuDTO>> querySpuDetailBySpuId(@NotNull Integer spuId) {
        return this.setResultSuccess(spuDetailMapper.selectByPrimaryKey(spuId));
    }

    @Override
    @Transactional
    public Result<JSONObject> save(SpuDTO spuDTO) {

        final Date date = new Date();//统一时间
        //新增spu
        SpuEntity spuEntity = CopyBean.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setSaleable(1);//默认上架
        spuEntity.setValid(1);//默认有效
        spuEntity.setCreateTime(date);
        spuEntity.setLastUpdateTime(date);
        spuMapper.insertSelective(spuEntity);
        //新增spuDetail
        SpuDetailEntity spuDetailEntity = CopyBean.copyProperties(spuDTO.getSpuDetail(), SpuDetailEntity.class);
        spuDetailEntity.setSpuId(spuEntity.getId());//spu返回主键
        spuDetailMapper.insertSelective(spuDetailEntity);
        insertSkuAndStock(spuDTO,spuEntity.getId(),date);//新增sku和stock
        return this.setResultSuccess("新增成功!");
    }

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
            copyspu.setCategoryName(categoryMapper.selectByIdList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(category ->{
                if(null!=category) return category.getName();
                return "";
            }).collect(Collectors.joining("/")));
            //查询品牌名称
            copyspu.setBrandName(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
            return copyspu;
        }).collect(Collectors.toList());
        PageInfo<SpuEntity> spuEntityPageInfo = new PageInfo<>(spuEntities); //完成分页
        return this.setResult(HTTPStatus.OK,spuEntityPageInfo.getTotal()+"",spuDTOList);
    }

    //删除sku和stock
    private void delSkuAndStock(Integer spuId){
        Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId",spuId);
        List<Long> skuIdList = skuMapper.selectByExample(example).stream().map(sku -> sku.getId()).collect(Collectors.toList());//skuId集合
        stockMapper.deleteByIdList(skuIdList);//删除stock
        skuMapper.deleteByIdList(skuIdList);//删除sku
    }

    //新增sku和stock
    private void insertSkuAndStock(SpuDTO spuDTO,Integer spuId,Date date){
        //新增sku(不能使用批量删除,因为需要返回skuId做Stock的ID)
        spuDTO.getSkus().stream().forEach(skuDTO -> {
            SkuEntity skuEntity = CopyBean.copyProperties(skuDTO, SkuEntity.class);
            skuEntity.setSpuId(spuId);
            skuEntity.setCreateTime(date);
            skuEntity.setLastUpdateTime(date);
            skuMapper.insertSelective(skuEntity);
            //新增stock
            StockEntity stockEntity = new StockEntity();
            stockEntity.setSkuId(skuEntity.getId());
            stockEntity.setStock(skuDTO.getStock());
            stockMapper.insertSelective(stockEntity);
        });
    }
}
