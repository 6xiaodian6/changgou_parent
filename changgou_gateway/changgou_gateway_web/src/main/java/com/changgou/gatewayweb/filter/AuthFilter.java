package com.changgou.gatewayweb.filter;

import com.changgou.gatewayweb.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-22 16:32:30
 **/
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    /**
     * 应该在配置文件里面配置这两个参数的值，然后在在这里进行值的注入操作,而不应该直接在class文件里面进行设置，这样不利于扩展
     */
    public static final String Authorization = "Authorization";
    private static final String LOGIN_URL="http://localhost:8001/api/oauth/toLogin";
    @Autowired
    private AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取当前请求对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        if ("/api/oauth/login".equals(path) || "/api/oauth/toLogin".equals(path)) {
            //放行
            return chain.filter(exchange);
        }
        //判断cookie上是否存在jti
        String jti = authService.getJtiFromCookie(request);
        if (StringUtils.isEmpty(jti)) {
            //返回登录页面
            return this.toLoginPage(LOGIN_URL+"?FROM="+request.getURI(),exchange);
        }
        //判断redis中token是否存在
        String redisToken = authService.getTokenFromRedis(jti);
        if (StringUtils.isEmpty(redisToken)){
            //拒绝访问,请求跳转
           return this.toLoginPage(LOGIN_URL+"?FROM="+request.getURI(),exchange);
        }
        //校验通过，请求增强，放行
        request.mutate().header(Authorization,"Bearer " + redisToken);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
    private Mono<Void> toLoginPage(String loginUrl,ServerWebExchange exchange){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location",loginUrl);
        return response.setComplete();
    }
}
