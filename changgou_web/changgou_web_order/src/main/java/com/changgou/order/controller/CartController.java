package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-25 08:28:33
 **/
@Controller
@RequestMapping("/wcart")
public class CartController {
    @Autowired
    private CartFeign cartFeign;

    /**
     * 查询购物车信息
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        Map map = cartFeign.list();
        model.addAttribute("items", map);
        return "cart";
    }

    /**
     * 添加购物车
     *
     * @param id  skuId
     * @param num sku数量
     * @return
     */
    @GetMapping("/add")
    @ResponseBody
    public Result<Map> add(@RequestParam("skuId") String id,@RequestParam("num") Integer num) {
        cartFeign.add(id, num);
        Map map = cartFeign.list();
        return new Result<>(true, StatusCode.OK, "添加购物车成功", map);
    }
}
