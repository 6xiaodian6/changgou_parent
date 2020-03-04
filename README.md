# changgou_parent项目简介

> 此项目为一个典型的电商项目，为用户提供一个完整的线上的购物环境，并进行支付操作。

# 技术栈

> SpringBoot、SpringCloud、Eureka、RabbitMQ、Spring Task、FastDFS、Redis、ElasticSearch、Spring Security、OAuth2第三方登录、CAS单点登录、JWT鉴权、通用Mapper、Canal日志监控、Gateway网关、微信支付、微信扫码登录、Vue.js、ElementUI、Thymeleaf

# 模块介绍

> -- changgou-user-oauth
>
> > 该模块是自定义第三方认证服务，作为第三方登录时的授权服务器使用
>
> -- changgou_canal
>
> > 该模块负责监控数据库数据变化的组件服务，当数据库数据发生变化时，该服务负责发送消息至消息队列
>
> -- changgou_common
>
> > 该模块是通用实体类，包括统一异常类和工具类
>
> -- changgou_common_db
>
> > 该模块是数据库连接通用模块，所有需要连接数据库的服务都需要继承此模块
>
> -- changgou_common_fescar
>
> > 该模块是分布式事务模块，需要使用到分布式事务的服务只需要依赖此模块即可
>
> -- changgou_eureka
>
> > 该模块是注册中心服务，负责服务的注册与拉取
>
> -- changgou_gateway
>
> > 该模块是网关服务，负责请求的拦截与转发
>
> -- changgou_service
>
> > 该模块是微服务模块，用于存放所有独立的微服务工程
>
> -- changgou_service_api
>
> > 该模块是对外提供依赖的模块，主要存放JavaBean、Feign以及Hystrix配置
>
> -- changgou_web
>
> > 该模块是web服务工程，如需要调用多个微服务，可以将他们写入到该模块中
