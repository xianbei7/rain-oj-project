package com.rain.oj.judgeservice;

import com.rain.oj.judgeservice.message.IntRabbitMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.rain.oj")
@EnableFeignClients(basePackages = {"com.rain.oj.feignclient.service"})
@EnableDiscoveryClient
public class RainOjJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RainOjJudgeServiceApplication.class, args);
    }

}
