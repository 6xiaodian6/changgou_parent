package com.changgou.service;

import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-28 20:52:51
 **/
public interface WxPayService {
    /**
     * 微信支付
     *
     * @param orderId
     * @param money
     * @return
     */
    Map nativePay(String orderId, Integer money);
}
