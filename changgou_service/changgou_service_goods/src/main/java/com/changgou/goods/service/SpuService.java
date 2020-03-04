package com.changgou.goods.service;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SpuService {

    /***
     * 查询所有
     * @return
     */
    List<Spu> findAll();

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    Spu findById(String id);

    /***
     * 新增
     * @param spu
     */
    void add(Spu spu);

    /***
     * 修改
     * @param spu
     */
    void update(Spu spu);

    /***
     * 物理删除
     * @param id
     */
    void realDelete(String id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<Spu> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Spu> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Spu> findPage(Map<String, Object> searchMap, int page, int size);

    /**
     * 新增商品信息，包含sku的操作
     *
     * @param goods
     */
    void add(Goods goods);

    /**
     * 根据商品id查询商品信息
     *
     * @param goodsId 商品id
     * @return 返回商品信息
     */
    Goods findGoodsById(String goodsId);

    /**
     * 更新商品信息
     *
     * @param goods
     */
    void update(Goods goods);

    /**
     * 根据商品id判断是否删除，如果没有删除就直接审核，并上架
     *
     * @param id
     */
    void audit(String id);

    /**
     * 根据商品id对商品进行下架操作
     *
     * @param id 商品id
     */
    void pull(String id);

    /**
     * 根据商品id对商品进行上架操作
     *
     * @param id 商品id
     */
    void put(String id);

    /**
     * 逻辑删除
     *
     * @param id 商品id
     */
    void delete(String id);

    /**
     * 商品还原
     *
     * @param id 商品id
     */
    void restore(String id);

    /**
     * 根据id查询spu的信息
     * @param id 商品信息id
     * @return
     */
    Spu findSpuById(String id);
}
