package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-24 20:13:34
 **/
@RestController
@CrossOrigin
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartservice;
    @Autowired
    private TokenDecode tokenDecode;
    @GetMapping("/add")
    public Result add(@RequestParam("skuId") String skuId,@RequestParam("num") Integer num){
        String username = tokenDecode.getUserInfo().get("username");
        cartservice.add(skuId,num,username);
        return new Result(true, StatusCode.OK,"加入购物车成功");
    }
    @GetMapping(value = "/list")
    public Map list(){
        String username = tokenDecode.getUserInfo().get("username");
        return cartservice.list(username);
    }
}
