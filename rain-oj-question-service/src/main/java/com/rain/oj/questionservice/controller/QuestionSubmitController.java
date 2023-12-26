package com.rain.oj.questionservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rain.oj.common.BaseResponse;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.ResultUtils;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.model.dto.questionsubmission.QuestionSubmissionAddRequest;
import com.rain.oj.model.dto.questionsubmission.QuestionSubmissionQueryRequest;
import com.rain.oj.model.dto.questionsubmission.UserQuestionSubmissionQueryRequest;
import com.rain.oj.model.entity.QuestionSubmission;
import com.rain.oj.model.entity.User;
import com.rain.oj.model.enums.ProgrammingLanguageEnum;
import com.rain.oj.model.vo.DetailedQuestionSubmissionVO;
import com.rain.oj.feignclient.service.UserFeignClient;
import com.rain.oj.model.vo.QuestionSubmissionVO;
import com.rain.oj.model.vo.ViewQuestionSubmissionVO;
import com.rain.oj.questionservice.service.QuestionSubmitService;
import com.rain.oj.questionservice.util.UserSubmitRateLimiter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题目收藏接口
 */
@RestController
@RequestMapping("/submit")
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;


    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private UserSubmitRateLimiter userSubmitRateLimiter;

    /**
     * 提交题目
     *
     * @param questionSubmissionAddRequest 题目提交请求
     * @param request                  http请求
     * @return {@link Long} 题目提交id
     */
    @PostMapping("/")
    public BaseResponse<Boolean> doQuestionSubmit(@RequestBody QuestionSubmissionAddRequest questionSubmissionAddRequest,
                                                  HttpServletRequest request) {
        if (questionSubmissionAddRequest == null || questionSubmissionAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userFeignClient.getLoginUser(request);
        Long userId = loginUser.getId();
        boolean allowSubmit = userSubmitRateLimiter.limitUserSubmit(userId);
        if (allowSubmit) {
            Boolean result = questionSubmitService.doQuestionSubmit(questionSubmissionAddRequest, userId);
            return ResultUtils.success(result);
        } else {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "操作过于频繁！请稍后再试");
        }
    }

    /**
     * 分页获取题目提交列表（仅管理员和本人能看到答案和提交代码）
     *
     * @param questionSubmissionQueryRequest 题目查询请求
     * @param request                    Http请求
     * @return {@link Page< DetailedQuestionSubmissionVO >} 题目提交vo分页
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<DetailedQuestionSubmissionVO>> listDetailedQuestionSubmissionByPage(@RequestBody QuestionSubmissionQueryRequest questionSubmissionQueryRequest,
                                                                                                 HttpServletRequest request) {
        long current = questionSubmissionQueryRequest.getCurrent();
        long size = questionSubmissionQueryRequest.getPageSize();
        Page<QuestionSubmission> questionSubmitPage = questionSubmitService.page(new Page<>(current, size), questionSubmitService.getDetailedQueryWrapper(questionSubmissionQueryRequest));
        // 判断是否登录
        userFeignClient.getLoginUser(request);
        // 返回脱敏信息
        return ResultUtils.success(questionSubmitService.getDetailedQuestionSubmitVOPage(questionSubmitPage));
    }

    /**
     * 分页获取某个用户题目提交列表
     *
     * @param userQuestionSubmissionQueryRequest 题目查询请求
     * @return {@link Page< QuestionSubmissionVO >} 简单题目提交vo分页
     */
    @PostMapping("/list/user/page")
    public BaseResponse<Page<QuestionSubmissionVO>> listUserQuestionSubmissionByPage(@RequestBody UserQuestionSubmissionQueryRequest userQuestionSubmissionQueryRequest) {
        long current = userQuestionSubmissionQueryRequest.getCurrent();
        long size = userQuestionSubmissionQueryRequest.getPageSize();
        Page<QuestionSubmission> questionSubmitPage = questionSubmitService.page(new Page<>(current, size), questionSubmitService.getUserQueryWrapper(userQuestionSubmissionQueryRequest.getUserId()));
        // 返回脱敏信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage));
    }

    /**
     * 获取用户某个题目的提交列表（仅自己和管理员）
     *
     * @param questionId 题目id
     * @param userId     用户id（可没有即本人查看）
     * @param request    Http请求
     * @return {@link List< ViewQuestionSubmissionVO >} 题目提交vo列表
     */
    @GetMapping("/my/view/list")
    public BaseResponse<List<ViewQuestionSubmissionVO>> listMyQuestionSubmissionById(Long questionId, Long userId, HttpServletRequest request) {
        if (questionId == null || questionId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目不存在");
        }
        final User loginUser = userFeignClient.getLoginUser(request);
        if (userId == null) {
            userId = loginUser.getId();
        } else if (!userId.equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无查看权限");
        }
        List<QuestionSubmission> questionSubmissionList = questionSubmitService.listMyQuestionSubmitById(questionId, userId);
        // 返回脱敏信息
        return ResultUtils.success(questionSubmitService.getViewQuestionSubmitVOList(questionSubmissionList));
    }

    /**
     * 获取题目提交语言
     *
     * @return {@link List<String>} 编程语言列表
     */
    @GetMapping("/get/languages")
    public BaseResponse<List<String>> getLanguages() {
        return ResultUtils.success(ProgrammingLanguageEnum.getLanguages());
    }
}
