package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-22 08:29:58
 **/
@FeignClient(name = "user")
public interface UserFeign {
    @GetMapping("/user/load/{username}")
    User findUserInfo(@PathVariable("username")String username);
}
