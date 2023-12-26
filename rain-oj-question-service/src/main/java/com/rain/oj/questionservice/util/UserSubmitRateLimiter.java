package com.rain.oj.questionservice.util;

import com.rain.oj.common.constant.QuestionConstant;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户提交限制器
 */
@Component
public class UserSubmitRateLimiter {

    private Integer maxRequests = 2;
    private Integer windowSeconds = 10;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public boolean limitUserSubmit(Long userId) {
        String key = QuestionConstant.USER_RATE_LIMITER_KEY + userId;
        // 获取当前时间戳
        long currentTimestamp = System.currentTimeMillis();
        // 获取窗口开始时间戳
        long windowStartTimestamp = currentTimestamp - windowSeconds * 1000;
        // 移除窗口外的记录
        stringRedisTemplate.opsForZSet().removeRangeByScore(key, 0, windowStartTimestamp);
        // 获取当前窗口内的请求次数
        Long requestCount = stringRedisTemplate.opsForZSet().count(key, windowStartTimestamp, currentTimestamp);
        // 判断是否超过限制
        boolean result = requestCount != null && requestCount < maxRequests;
        // 添加当前请求的记录
        if (result) {
            stringRedisTemplate.opsForZSet().add(key, String.valueOf(currentTimestamp), currentTimestamp);
        }
        return result;
    }
}
