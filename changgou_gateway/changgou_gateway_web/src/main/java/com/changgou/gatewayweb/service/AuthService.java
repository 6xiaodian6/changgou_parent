package com.changgou.gatewayweb.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-22 18:31:13
 **/
@Service
public class AuthService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 判断cookie中jti是否存在
     *
     * @param request
     * @return
     */
    public String getJtiFromCookie(ServerHttpRequest request) {
        HttpCookie cookie = request.getCookies().getFirst("uid");
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 判断redis中令牌是否过期
     *
     * @param jti
     * @return
     */
    public String getTokenFromRedis(String jti) {
        String token = stringRedisTemplate.boundValueOps(jti).get();
        return token;
    }
}
