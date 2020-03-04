package com.changgou.order.service.impl;

import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-24 20:18:07
 **/
@Service
public class CartServiceImpl implements CartService {
    private static final String CART = "Cart_";
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SpuFeign spuFeign;

    /**
     * 添加购物车
     *
     * @param skuId    商品id
     * @param num      数量
     * @param username 用户名
     */
    @Override
    public void add(String skuId, Integer num, String username) {
        /**
         * 1.查询redis中的数据
         * 2.如果redis中已经存在，则增加数量，重新计算金额
         * 3.如果没有，将商品添加到缓存
         */
        OrderItem orderItem = (OrderItem) redisTemplate.boundHashOps(CART + username).get(skuId);
        if (orderItem != null) {
            //存在，则刷新购物车
            orderItem.setNum(orderItem.getNum() + num);
            if (orderItem.getNum() <= 0) {
                redisTemplate.boundHashOps(CART + username).delete(skuId);
                return;
            }
            orderItem.setMoney(orderItem.getNum() * orderItem.getPrice());
            orderItem.setPayMoney(orderItem.getNum() * orderItem.getPrice());
        } else {
            //不存在，新增购物车
            Result<Sku> skuResult = skuFeign.findById(skuId);
            Sku sku = skuResult.getData();
            Spu spu = spuFeign.findSpuById(sku.getSpuId()).getData();
            //将sku转换为OrderItem
            orderItem = this.sku2OrderItem(sku, spu, num);
        }
        //存入redis
        redisTemplate.boundHashOps(CART + username).put(skuId, orderItem);

    }

    /**
     * 根据用户名查询购物车商品信息
     *
     * @param username
     * @return
     */
    @Override
    public Map list(String username) {
        Map<Object, Object> map = new HashMap<>();
        List<OrderItem> orderItemList = redisTemplate.boundHashOps(CART + username).values();
        map.put("orderItemList", orderItemList);
        //商品数量与总价格
        Integer totalNum = 0;
        Integer totalPrice = 0;
        for (OrderItem orderItem : orderItemList) {
            totalNum += orderItem.getNum();
            totalPrice += orderItem.getMoney();
        }
        map.put("totalNum", totalNum);
        map.put("totalPrice", totalPrice);
        map.put("username",username);
        return map;
    }

    /**
     * 将sku数据转换为orderItem
     *
     * @param sku
     * @param spu
     * @param num
     * @return
     */
    private OrderItem sku2OrderItem(Sku sku, Spu spu, Integer num) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        //总金额，单价乘以数量
        orderItem.setMoney(num * orderItem.getPrice());
        //实付金额也是单价乘以数量
        orderItem.setPayMoney(num * orderItem.getPrice());
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight() * num);
        //分类id设置
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}
