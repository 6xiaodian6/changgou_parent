package com.changgou.search.service;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-16 08:20:22
 **/
public interface ESManagerService {
    /**
     * 创建索引结构
     */
    void createIndexAndMapping();

    /**
     * 导入全部数据到ES索引库中
     */
    void importAll();

    /**
     * 根据spuId导入数据到ES索引库中
     *
     * @param spuId 商品id
     */
    void importDataToESBySpuId(String spuId);

    /**
     * 根据spuId删除数据
     *
     * @param spuId
     */
    void delSkuListBySpuId(String spuId);
}
