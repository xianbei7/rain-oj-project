package com.rain.oj.questionservice.config;

import com.rain.oj.common.constant.JudgeConstant;
import org.redisson.Redisson;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {
    @Value("${judge.rate-limit.permits}")
    private Integer permits;

    @Value("${judge.rate-limit.time-interval}")
    private Integer timeInterval;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private Integer database;

    @Bean
    public RRateLimiter redissonRateLimiter() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%s",host,port))
                .setPassword(password)
                .setDatabase(database);
        RedissonClient redissonClient = Redisson.create(config);
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(JudgeConstant.RATE_LIMITER_NAME);
        rateLimiter.trySetRate(RateType.OVERALL, permits, timeInterval, RateIntervalUnit.SECONDS);
        return rateLimiter;
    }
}
