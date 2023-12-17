package com.rain.oj.feignclient.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务
 */
@FeignClient(name = "judge-service", path = "/api/judge/feign")
public interface JudgeFeignClient {

    /**
     * 根据id 获取用户
     *
     * @param questionSubmitId 题目提交id
     * @return {@link Boolean} 是否进行判题
     */
    @GetMapping("/do")
    Boolean doJudge(@RequestParam("questionSubmitId") Long questionSubmitId);
}
