package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-21 20:20:09
 **/
public interface AuthService {
    AuthToken login(String username,String password,String clientId,String clientSecret);
}
