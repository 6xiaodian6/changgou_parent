package com.changgou.page;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-20 10:52:39
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.changgou.goods.feign")
public class PageApplication {
    public static void main(String[] args) {
        SpringApplication.run(PageApplication.class,args);
    }
}
