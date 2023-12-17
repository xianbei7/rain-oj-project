package com.rain.oj.questionservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rain.oj.common.BaseResponse;
import com.rain.oj.common.DeleteRequest;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.ResultUtils;
import com.rain.oj.common.annotation.AuthCheck;
import com.rain.oj.common.constant.UserConstant;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.common.exception.ThrowUtils;
import com.rain.oj.model.dto.question.QuestionQueryRequest;
import com.rain.oj.model.dto.question.QuestionSaveRequest;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.entity.User;
import com.rain.oj.model.enums.QuestionTagEnum;
import com.rain.oj.model.vo.DoQuestionVO;
import com.rain.oj.model.vo.QuestionVO;
import com.rain.oj.feignclient.service.UserFeignClient;
import com.rain.oj.questionservice.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题目接口
 */
@RestController
@RequestMapping("/")
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param questionSaveRequest 题目保存请求
     * @param request             Http请求
     * @return {@link Boolean} 是否成功
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addQuestion(@RequestBody QuestionSaveRequest questionSaveRequest, HttpServletRequest request) {
        if (questionSaveRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        Boolean result = questionService.saveQuestion(questionSaveRequest, loginUser, true);
        return ResultUtils.success(result);
    }

    /**
     * 删除
     *
     * @param deleteRequest 题目删除请求
     * @param request       Http请求
     * @return {@link Boolean} 是否成功
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userFeignClient.isAdmin(user)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = questionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionSaveRequest 题目保存请求
     * @return {@link Boolean} 是否成功
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionSaveRequest questionSaveRequest, HttpServletRequest request) {
        if (questionSaveRequest == null || questionSaveRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        Boolean result = questionService.saveQuestion(questionSaveRequest, loginUser, false);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id 题目id
     * @return {@link Question} 题目
     */
    @GetMapping("/get")
    public BaseResponse<Question> getQuestionById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        if (!question.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(question);
    }

    /**
     * 根据 id 获取
     *
     * @param id 题目id
     * @return {@link QuestionVO} 题目vo
     */
    @GetMapping("/get/vo")
    public BaseResponse<DoQuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest 题目查询请求
     * @param request              Http请求
     * @return {@link Page<QuestionVO>} 题目vo分页
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                               HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionQueryRequest 题目查询请求
     * @param request              Http请求
     * @return {@link Page<QuestionVO>} 题目vo分页
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                 HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    // endregion

    /**
     * 分页获取题目提交列表（仅管理员和本人能看到答案和提交代码）
     *
     * @param questionQueryRequest 题目查询请求
     * @param request              Http请求
     * @return {@link Page<Question>} 题目分页
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Question>> listQuestionSubmitByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                 HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    /**
     * 获取题目标签
     *
     * @return {@link List<String>} 题目标签
     */
    @GetMapping("/get/tags")
    public BaseResponse<List<String>> getQuestionTags() {
        return ResultUtils.success(QuestionTagEnum.getTags());
    }

}
