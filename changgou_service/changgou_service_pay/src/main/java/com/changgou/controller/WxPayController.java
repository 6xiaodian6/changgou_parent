package com.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-28 20:49:18
 **/
@RestController
@RequestMapping("/wxpay")
public class WxPayController {
    @Autowired
    private WxPayService wxPayService;
    @GetMapping("/nativePay")
    public Result nativePay(@RequestParam("orderId")String orderId,@RequestParam("money")Integer money){
        Map map = wxPayService.nativePay(orderId,money);
        return new Result(true, StatusCode.OK,"",map);
    }
}
