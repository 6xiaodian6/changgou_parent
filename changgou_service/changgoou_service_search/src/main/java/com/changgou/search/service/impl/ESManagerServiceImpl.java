package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.ESManagerMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.ESManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-16 08:25:18
 **/
@Service
public class ESManagerServiceImpl implements ESManagerService {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private ESManagerMapper esManagerMapper;

    @Override
    public void createIndexAndMapping() {
        //创建索引，根据传参的类进行构建索引
        elasticsearchTemplate.createIndex(SkuInfo.class);
        //创建映射，根据传参的类进行映射的构建，默认是该类中的属性进行构建的
        elasticsearchTemplate.putMapping(SkuInfo.class);
    }

    @Override
    public void importAll() {
        List<Sku> skuList = skuFeign.findListSpuById("all");
        if (skuList == null || skuList.size() <= 0) {
            throw new RuntimeException("当前商品不存在");
        }
        //将sku转换为json字符串，相当于是序列化
        String skuJsonList = JSON.toJSONString(skuList);
        //将json转换为索引库对象，相当于是反序列化，但是反序列化后的对象却不是原来的那个了，这个就是深克隆的一种方式
        List<SkuInfo> skuInfoList = JSON.parseArray(skuJsonList, SkuInfo.class);
        //将规格转换为map
        skuInfoList.forEach(skuInfo -> skuInfo.setSpecMap(JSON.parseObject(skuInfo.getSpec(), Map.class)));
        //导入索引库
        esManagerMapper.saveAll(skuInfoList);
    }

    /**
     * 根据商品id将数据导入到es中
     * @param spuId 商品id
     */
    @Override
    public void importDataToESBySpuId(String spuId) {
        //根据spuId查询相关的sku列表
        List<Sku> skuList = skuFeign.findListSpuById(spuId);
        if (skuList ==null|| skuList.size()<=0){
            throw new RuntimeException("当前商品无数据，无法导入索引库：" + spuId);
        }
        String skuJsonList = JSON.toJSONString(skuList);
        List<SkuInfo> skuInfoList = JSON.parseArray(skuJsonList, SkuInfo.class);
        skuInfoList.forEach(skuInfo -> skuInfo.setSpecMap(JSON.parseObject(skuInfo.getSpec(),Map.class)));
        esManagerMapper.saveAll(skuInfoList);
    }

    @Override
    public void delSkuListBySpuId(String spuId) {
        List<Sku> skuList = skuFeign.findListSpuById(spuId);
        for (Sku sku : skuList) {
            esManagerMapper.deleteById(Long.parseLong(sku.getId()));
        }
    }

}
