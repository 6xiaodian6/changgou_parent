package com.changgou.order.service;

import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-24 20:18:01
 **/
public interface CartService {
    /**
     * 添加商品到购物车
     * @param skuId
     * @param num
     * @param username
     */
    void add(String skuId, Integer num, String username);

    /**
     * 根据用户名查询购物车商品
     * @param username
     * @return
     */
    Map list(String username);
}
