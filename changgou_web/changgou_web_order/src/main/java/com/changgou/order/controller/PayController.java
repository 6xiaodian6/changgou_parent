package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.pay.feign.WxPayFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-28 21:13:03
 **/
@Controller
@RequestMapping("/wxpay")
public class PayController {
    @Autowired
    private OrderFeign orderFeign;
    @Autowired
    private WxPayFeign wxPayFeign;

    @GetMapping
    public String wxPay(String orderId, Model model) {
        //根据orderId查询订单
        Result orderResult = orderFeign.findById(orderId);
        if (orderResult.getData() == null) {
            //跳转至出错页面
            return "fail";
        }
        Order order = (Order) orderResult.getData();
        //判断支付状态
        if ("1".equals(order.getPayStatus())) {
            //如果不是未支付订单，跳转至出错页面
            return "fail";
        }
        Result payResult = wxPayFeign.nativePay(orderId, order.getPayMoney());
        if (payResult.getData() == null) {
            return "fail";
        }
        Map payMap = (Map) payResult.getData();
        payMap.put("payMoney", order.getPayMoney());
        payMap.put("orderId", orderId);
        model.addAttribute(payMap);
        return "wxpay";
    }
}
