package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.AddressFeign;
import com.changgou.user.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-26 11:42:04
 **/
@Controller
@CrossOrigin
@RequestMapping(value = "/worder")
public class OrderController {
    @Autowired
    private AddressFeign addressFeign;
    @Autowired
    private CartFeign cartFeign;
    @Autowired
    private OrderFeign orderFeign;

    @RequestMapping(value = "/ready/order")
    public String readyOrder(Model model) {
        Result<List<Address>> addressList = addressFeign.list();
        for (Address addr : addressList.getData()) {
            if ("1".equals(addr.getIsDefault())) {
                model.addAttribute("deAddr", addr);
                break;
            }
        }
        model.addAttribute("address", addressList);
        Map map = cartFeign.list();
        List<OrderItem> orderItemList = (List<OrderItem>) map.get("orderItemList");
        Integer totalPrice = (Integer) map.get("totalPrice");
        Integer totalNum = (Integer) map.get("totalNum");
        String username = (String) map.get("username");
        model.addAttribute("carts", orderItemList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("username", username);
        return "order";
    }

    @PostMapping(value = "/add")
    @ResponseBody
    public Result add(@RequestBody Order order) {
        Result result = orderFeign.add(order);
        return result;
    }

    /**
     * 根据订单id跳转到支付页面
     *
     * @param orderId 订单id
     * @param model
     * @return
     */
    @GetMapping("/toPayPage")
    public String toPayPage(String orderId, Model model) {
        Result<Order> orderResult = orderFeign.findById(orderId);
        Order order = orderResult.getData();
        model.addAttribute("orderId",orderId);
        model.addAttribute("payMoney",order.getPayMoney());
        return "pay";
    }
}
