package com.changgou.pay.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-28 21:10:32
 **/
@FeignClient("pay")
public interface WxPayFeign {
    /**
     * 微信支付接口
     * @param orderId
     * @param money
     * @return
     */
    @GetMapping("/wxpay/nativePay")
    Result nativePay(@PathVariable("orderId")String orderId,@PathVariable("money")Integer money);
}
