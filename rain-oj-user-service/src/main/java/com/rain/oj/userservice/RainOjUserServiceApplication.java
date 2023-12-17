package com.rain.oj.userservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.rain.oj.userservice.mapper")
@ComponentScan("com.rain.oj")
@EnableFeignClients(basePackages = {"com.rain.oj.feignclient.service"})
@EnableDiscoveryClient
public class RainOjUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RainOjUserServiceApplication.class, args);
    }

}
