package com.rain.oj.questionservice.controller;

import com.rain.oj.common.BaseResponse;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.ResultUtils;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.questionservice.service.QuestionTemplateService;
import com.rain.oj.feignclient.service.UserFeignClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目收藏接口
 */
@RestController
@RequestMapping("/template")
public class QuestionTemplateController {

    @Resource
    private QuestionTemplateService questionTemplateService;

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 根据questionId和language获取代码模板
     *
     * @param questionId 题目id
     * @param language 编程语言
     * @param request Http请求
     * @return {@link String} 代码模板
     */
    @GetMapping("/")
    public BaseResponse<String> getQuestionTemplate(Long questionId, String language, HttpServletRequest request) {
        if (questionId == null || questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(language)) {
            return ResultUtils.success("");
        }
        // 登录才能操作
        userFeignClient.getLoginUser(request);
        return ResultUtils.success(questionTemplateService.getCodeTemplate(questionId, language));
    }
}
