package com.init.item.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * @author wu lele
 * @since 2024/7/17 16:22
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableR2dbcRepositories(basePackages = "com.init.item.dao")
@ComponentScan(basePackages = {"com.init.item"})
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
