# WebFlux Initialize Project Demo

WebFlux的初始化项目Demo，SpringBoot3 + WebFlux

已完成引入/实现：

* 全局异常捕获，继承AbstractErrorWebExceptionHandler、DefaultErrorAttributes
* 接口出入参日志记录，继承ServerWebExchangeDecorator、ServerHttpRequestDecorator、ServerHttpResponseDecorator
* 接口限流
* 注解式、编程式接口Demo
* r2dbc-mysql使用Demo
* 文档聚合，knife4j-gateway引入

TODO：

* WebClient
* Auth
* server-api
* mongodb
* mybatis-r2dbc