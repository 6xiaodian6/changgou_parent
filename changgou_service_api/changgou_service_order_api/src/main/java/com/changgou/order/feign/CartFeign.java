package com.changgou.order.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-25 08:18:33
 **/
@FeignClient(name = "order")
public interface CartFeign {
    /**
     * 添加购物车
     *
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping("/cart/add")
    Result add(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num);

    /**
     * 查询用户购物车列表
     *
     * @return
     */
    @GetMapping(path = "/cart/list")
    Map list();
}
