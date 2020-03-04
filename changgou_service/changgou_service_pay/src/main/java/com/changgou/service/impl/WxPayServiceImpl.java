package com.changgou.service.impl;

import com.changgou.service.WxPayService;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-28 20:55:40
 **/
@Service
public class WxPayServiceImpl implements WxPayService {
    @Autowired
    private WXPay wxPay;
    @Override
    public Map nativePay(String orderId, Integer money) {
        try{
            //1.封装请求参数
            Map<String, String> map = new HashMap<>();
            //商品描述
            map.put("body","畅购商城");
            //订单号
            map.put("out_trade_no",orderId);
            BigDecimal paymoney = new BigDecimal("0.01");
            //1.00
            BigDecimal fen = paymoney.multiply(new BigDecimal("100"));
            //转换为1
            fen = fen.setScale(0, BigDecimal.ROUND_UP);
            map.put("total_fee",String.valueOf(fen));
            //终端IP
            map.put("spbill_create_ip","127.0.0.1");
            //回调地址
            map.put("notify_url","http://www.itcast.cn");
            //交易类型
            map.put("trade_type","NATIVE");
            Map<String, String> mapResult = wxPay.unifiedOrder(map);
            return mapResult;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
