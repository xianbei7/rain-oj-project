package com.rain.oj.questionservice.controller;

import com.rain.oj.common.BaseResponse;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.ResultUtils;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.model.entity.User;
import com.rain.oj.questionservice.service.QuestionThumbService;
import com.rain.oj.feignclient.service.UserFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 */
@RestController
@RequestMapping("/thumb")
public class QuestionThumbController {

    @Resource
    private QuestionThumbService questionThumbService;

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 点赞 / 取消点赞
     *
     * @param questionId 题目id
     * @param request Http请求
     * @return {@link Integer} 点赞状态
     */
    @GetMapping("/")
    public BaseResponse<Integer> doThumb(Long questionId,HttpServletRequest request) {
        if (questionId == null || questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userFeignClient.getLoginUser(request);
        int result = questionThumbService.doQuestionThumb(questionId, loginUser);
        return ResultUtils.success(result);
    }

}
