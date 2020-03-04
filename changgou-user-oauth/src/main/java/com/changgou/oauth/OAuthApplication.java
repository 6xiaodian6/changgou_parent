package com.changgou.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-20 20:20:59
 **/
@SpringBootApplication
@EnableDiscoveryClient
//@MapperScan(basePackages = "com.changgou.auth.dao")
@EnableFeignClients(basePackages = {"com.changgou.user.feign"})
public class OAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuthApplication.class,args);
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
