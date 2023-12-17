package com.rain.oj.questionservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rain.oj.common.BaseResponse;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.ResultUtils;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.common.exception.ThrowUtils;
import com.rain.oj.model.bo.QuestionFavourBO;
import com.rain.oj.model.dto.question.QuestionQueryRequest;
import com.rain.oj.model.dto.questionfavour.QuestionFavourQueryRequest;
import com.rain.oj.model.entity.User;
import com.rain.oj.model.vo.QuestionFavourVO;
import com.rain.oj.model.vo.QuestionVO;
import com.rain.oj.questionservice.service.QuestionFavourService;
import com.rain.oj.feignclient.service.UserFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目收藏接口
 */
@RestController
@RequestMapping("/favour")
public class QuestionFavourController {

    @Resource
    private QuestionFavourService questionFavourService;

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 收藏 / 取消收藏
     *
     * @param questionId 题目id
     * @param request    Http请求
     * @return {@link Integer} 收藏状态
     */
    @GetMapping("/")
    public BaseResponse<Integer> doQuestionFavour(Long questionId, HttpServletRequest request) {
        if (questionId == null || questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userFeignClient.getLoginUser(request);
        int result = questionFavourService.doQuestionFavour(questionId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取我收藏的题目列表
     *
     * @param questionQueryRequest 题目查询请求
     * @param request              Http请求
     * @return {@link Page<QuestionVO>}
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<QuestionVO>> listMyFavourQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                     HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        /*Page<Question> questionPage = questionFavourService.listFavourQuestionByPage((current - 1) * size, size, loginUser.getId());
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));*/
        return ResultUtils.success(null);
    }

    /**
     * 获取用户收藏的题目列表
     *
     * @param questionFavourQueryRequest 题目收藏查询请求
     * @param request                    Http请求
     * @return {@link Page<QuestionFavourVO>} 题目收藏vo分页
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionFavourVO>> listFavourQuestionByPage(@RequestBody QuestionFavourQueryRequest questionFavourQueryRequest,
                                                                         HttpServletRequest request) {
        if (questionFavourQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = questionFavourQueryRequest.getCurrent();
        long size = questionFavourQueryRequest.getPageSize();
        Long userId = questionFavourQueryRequest.getUserId();
        // 限制爬虫
        ThrowUtils.throwIf(size > 50 || userId == null, ErrorCode.PARAMS_ERROR);
        Page<QuestionFavourBO> questionPage = questionFavourService.listFavourQuestionByPage(new Page<>(current, size), userId);
        return ResultUtils.success(questionFavourService.getFavourQuestionVOPage(questionPage, request));
    }
}
