package com.rain.oj.questionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rain.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.rain.oj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.rain.oj.model.entity.QuestionSubmit;
import com.rain.oj.model.entity.User;
import com.rain.oj.model.vo.DetailedQuestionSubmitVO;
import com.rain.oj.model.vo.QuestionSubmitVO;
import com.rain.oj.model.vo.ViewQuestionSubmitVO;

import java.util.List;

public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest 题目提交请求
     * @param loginUser                登录用户
     * @return {@link Long} 题目提交id
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest 题目提交查询条件
     * @return {@link LambdaQueryWrapper <QuestionSubmit>} 查询条件
     */
    LambdaQueryWrapper<QuestionSubmit> getDetailedQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取用户某个题目的提交记录
     *
     * @param questionId 题目id
     * @param userId     用户id
     * @return {@link List<QuestionSubmit>} 题目提交列表
     */
    List<QuestionSubmit> listMyQuestionSubmitById(Long questionId, Long userId);

    /**
     * 获取查询条件
     *
     * @param userId 用户id
     * @return {@link LambdaQueryWrapper<QuestionSubmit>} 查询条件
     */
    LambdaQueryWrapper<QuestionSubmit> getUserQueryWrapper(Long userId);

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage 题目提交分页
     * @return {@link Page < DetailedQuestionSubmitVO >} 题目提交vo分页
     */
    Page<DetailedQuestionSubmitVO> getDetailedQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage);

    /**
     * 分页获取简单题目提交封装
     *
     * @param questionSubmitPage 题目提交分页
     * @return {@link Page<DetailedQuestionSubmitVO>} 题目提交vo分页
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage);

    /**
     * 获取某个题目的提交记录列表
     *
     * @param questionSubmitList 题目提交列表
     * @return {@link Page< ViewQuestionSubmitVO >} 题目提交vo列表
     */
    List<ViewQuestionSubmitVO> getViewQuestionSubmitVOList(List<QuestionSubmit> questionSubmitList);
}
