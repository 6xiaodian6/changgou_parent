package com.changgou.order.feign;

import com.changgou.entity.Result;
import com.changgou.order.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-26 19:10:35
 **/
@FeignClient(name = "order")
public interface OrderFeign {
    /**
     * 提交订单数据
     * @param order
     * @return
     */
    @PostMapping("/order")
    Result add(@RequestBody Order order);

    /**
     * 通过订单id跳转支付页面
     * @param id
     * @return
     */
    @GetMapping("/order/{id}")
    Result findById(@PathVariable("id") String id);
}
