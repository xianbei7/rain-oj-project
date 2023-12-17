package com.rain.oj.questionservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rain.oj.common.BaseResponse;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.ResultUtils;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.rain.oj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.rain.oj.model.dto.questionsubmit.UserQuestionSubmitQueryRequest;
import com.rain.oj.model.entity.QuestionSubmit;
import com.rain.oj.model.entity.User;
import com.rain.oj.model.enums.ProgrammingLanguageEnum;
import com.rain.oj.model.vo.DetailedQuestionSubmitVO;
import com.rain.oj.feignclient.service.UserFeignClient;
import com.rain.oj.model.vo.QuestionSubmitVO;
import com.rain.oj.model.vo.ViewQuestionSubmitVO;
import com.rain.oj.questionservice.service.QuestionSubmitService;
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

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest 题目提交请求
     * @param request                  http请求
     * @return {@link Long} 题目提交id
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userFeignClient.getLoginUser(request);
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取题目提交列表（仅管理员和本人能看到答案和提交代码）
     *
     * @param questionSubmitQueryRequest 题目查询请求
     * @param request                    Http请求
     * @return {@link Page< DetailedQuestionSubmitVO >} 题目提交vo分页
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<DetailedQuestionSubmitVO>> listDetailedQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                                         HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size), questionSubmitService.getDetailedQueryWrapper(questionSubmitQueryRequest));
        // 判断是否登录
        userFeignClient.getLoginUser(request);
        // 返回脱敏信息
        return ResultUtils.success(questionSubmitService.getDetailedQuestionSubmitVOPage(questionSubmitPage));
    }

    /**
     * 分页获取某个用户题目提交列表
     *
     * @param userQuestionSubmitQueryRequest 题目查询请求
     * @return {@link Page< QuestionSubmitVO >} 简单题目提交vo分页
     */
    @PostMapping("/list/user/page")
    public BaseResponse<Page<QuestionSubmitVO>> listUserQuestionSubmitByPage(@RequestBody UserQuestionSubmitQueryRequest userQuestionSubmitQueryRequest) {
        long current = userQuestionSubmitQueryRequest.getCurrent();
        long size = userQuestionSubmitQueryRequest.getPageSize();
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size), questionSubmitService.getUserQueryWrapper(userQuestionSubmitQueryRequest.getUserId()));
        // 返回脱敏信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage));
    }

    /**
     * 获取用户某个题目的提交列表（仅自己和管理员）
     *
     * @param questionId 题目id
     * @param userId     用户id（可没有即本人查看）
     * @param request    Http请求
     * @return {@link List<ViewQuestionSubmitVO>} 题目提交vo列表
     */
    @GetMapping("/my/view/list")
    public BaseResponse<List<ViewQuestionSubmitVO>> listMyQuestionSubmitById(Long questionId, Long userId, HttpServletRequest request) {
        if (questionId == null || questionId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目不存在");
        }
        final User loginUser = userFeignClient.getLoginUser(request);
        if (userId == null) {
            userId = loginUser.getId();
        } else if (!userId.equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无查看权限");
        }
        List<QuestionSubmit> questionSubmitList = questionSubmitService.listMyQuestionSubmitById(questionId, userId);
        // 返回脱敏信息
        return ResultUtils.success(questionSubmitService.getViewQuestionSubmitVOList(questionSubmitList));
    }

    /**
     * 获取题目提交语言
     *
     * @return {@link List<String>} 编程语言列表
     */
    @GetMapping("/get/languages")
    public BaseResponse<List<String>> getQuestionSubmitLanguages() {
        return ResultUtils.success(ProgrammingLanguageEnum.getLanguages());
    }
}
