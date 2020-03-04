package com.changgou.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

/**
 * @description: 网关启动器
 * @author: yuandian
 * @createTime: 2019-11-09 21:13:41
 **/
@SpringBootApplication
@EnableEurekaClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }

    /**
     * 标注@Bean注释相当于是向容器中添加这个javabean，而这个方法的返回值就是需要添加的bean类型
     * 这是springboot常用的代替配置的文件注入bean的方式
     * @return 返回注入到容器中的bean对象
     */
    @Bean
    public KeyResolver ipKeyResolver(){
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }
}
