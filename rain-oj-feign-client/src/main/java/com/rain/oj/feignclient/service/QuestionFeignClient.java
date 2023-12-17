package com.rain.oj.feignclient.service;

import com.rain.oj.common.BaseResponse;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 题目接口
 */
@FeignClient(name = "question-service", path = "/api/question/feign")
public interface QuestionFeignClient {

    /**
     * 根据题目id获取题目
     *
     * @param questionId 题目id
     * @return {@link Question}题目
     */
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") Long questionId);


    /**
     * 根据id获取题目提交
     *
     * @param questionSubmitId 题目提交id
     * @return {@link QuestionSubmit} 题目提交
     */
    @GetMapping("/submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") Long questionSubmitId);

    /**
     * 更新题目提交
     * @param questionSubmit 题目提交
     * @return {@link boolean} 是否更新成功
     */
    @PostMapping("/submit/update")
    BaseResponse<Boolean> updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);
}
