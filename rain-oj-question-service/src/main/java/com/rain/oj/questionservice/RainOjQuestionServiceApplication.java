package com.rain.oj.questionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.rain.oj.questionservice.mapper")
@ComponentScan("com.rain.oj")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableScheduling
@EnableFeignClients(basePackages = {"com.rain.oj.feignclient.service"})
@EnableDiscoveryClient
public class RainOjQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RainOjQuestionServiceApplication.class, args);
    }

}
