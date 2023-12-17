package com.rain.oj.questionservice.controller.feign;

import com.rain.oj.common.BaseResponse;
import com.rain.oj.common.ResultUtils;
import com.rain.oj.model.entity.Question;
import com.rain.oj.feignclient.service.QuestionFeignClient;
import com.rain.oj.model.entity.QuestionSubmit;
import com.rain.oj.questionservice.service.QuestionService;
import com.rain.oj.questionservice.service.QuestionSubmitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 服务间调用-题目服务
 */
@RestController
@RequestMapping("/feign")
public class QuestionFeignController implements QuestionFeignClient {
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionService questionService;

    /**
     * 根据题目id获取题目
     *
     * @param questionId 题目id
     * @return {@link Question}题目
     */
    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") Long questionId) {
        return questionService.getById(questionId);
    }
    /**
     * 根据id获取题目提交
     *
     * @param questionSubmitId 题目提交id
     * @return {@link QuestionSubmit} 题目提交
     */
    @Override
    @GetMapping("/submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") Long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    /**
     * 更新题目提交
     *
     * @param questionSubmit 题目提交
     * @return {@link boolean} 是否更新成功
     */
    @Override
    @PostMapping("/submit/update")
    public BaseResponse<Boolean> updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return ResultUtils.success(questionSubmitService.updateById(questionSubmit));
    }
}
