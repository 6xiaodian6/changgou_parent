package com.changgou.user.feign;

import com.changgou.entity.Result;
import com.changgou.user.pojo.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-26 11:47:41
 **/
@FeignClient(name = "user")
@RequestMapping("/address")
public interface AddressFeign {
    /**
     * 查询用户所有收获地址
     *
     * @return
     */
    @GetMapping(value = "/list")
    Result<List<Address>> list();
}
