package com.changgou.oauth.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.oauth.service.AuthService;
import com.changgou.oauth.util.AuthToken;
import com.changgou.oauth.util.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-21 21:01:50
 **/
@Controller
@RequestMapping("/oauth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Value("${auth.clientId}")
    private String clientId;
    @Value("${auth.clientSecret}")
    private String clientSecret;
    @Value("${auth.cookieDomain}")
    private String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;

    @ResponseBody
    @RequestMapping("/login")
    public Result login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            return new Result(false, StatusCode.LOGINERROR, "登录失败，用户名或密码不存在");
        }
        if (StringUtils.isEmpty(password)) {
            return new Result(false, StatusCode.LOGINERROR, "登录失败，用户名或密码不存在");
        }
        AuthToken authToken = null;
        try {
            authToken = authService.login(username, password, clientId, clientSecret);
        } catch (Exception e) {
            return new Result(false, StatusCode.LOGINERROR, "登录失败，出现了异常");
        }
        this.saveJtiToCookie(authToken.getJti());
        return new Result(true, StatusCode.OK, "登录成功");
    }

    private void saveJtiToCookie(String jti) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", jti, cookieMaxAge, false);
    }

    @RequestMapping("/toLogin")
    public String toLogin(@RequestParam(value = "FROM", required = false, defaultValue = "") String from, Model model) {
        model.addAttribute("from", from);
        return "login";
    }
}
