package com.changgou.token;

import com.sun.jndi.toolkit.url.Uri;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-21 19:55:29
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TokenTest {
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 发送http请求创建令牌
     */
    @Test
    public void testCreateToken() {
        //采用客户端负载均衡，从eureka获取认证服务的ip端口
        ServiceInstance serviceInstance = loadBalancerClient.choose("USER_AUTH");
        URI uri = serviceInstance.getUri();
        //申请令牌地址
        String authUrl = uri + "/oauth/token";
        //1.header信息，包括http basic认证信息
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        //进行Base64编码，并将编码后的认证数据放到头文件中
        String httpbasic = httpbasic("changgou", "changgou");
        headers.add("Authorization", httpbasic);
        //2.指定认证类型、账号、密码
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", "itheima");
        body.add("password", "123456");
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<>(body, headers);
        //指定restTemplate，当遇到400或401响应的时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应值为400或者401的时候也要正常响应不能抛出异常
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });
        //远程调用申请令牌
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        Map result = exchange.getBody();
        System.out.println(result);
    }

    /**
     * 获取Base64编码信息
     *
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String httpbasic(String clientId, String clientSecret) {
        //将客户端id和客户端密码拼接，按客户端id：客户端密码
        String string = clientId + ":" + clientSecret;
        //进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Base " + new String(encode);
    }
}
