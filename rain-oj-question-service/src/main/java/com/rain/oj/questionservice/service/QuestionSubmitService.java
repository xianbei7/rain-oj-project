package com.rain.oj.questionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rain.oj.model.dto.questionsubmission.QuestionSubmissionAddRequest;
import com.rain.oj.model.dto.questionsubmission.QuestionSubmissionQueryRequest;
import com.rain.oj.model.entity.QuestionSubmission;
import com.rain.oj.model.vo.DetailedQuestionSubmissionVO;
import com.rain.oj.model.vo.QuestionSubmissionVO;
import com.rain.oj.model.vo.ViewQuestionSubmissionVO;

import java.util.List;

public interface QuestionSubmitService extends IService<QuestionSubmission> {

    /**
     * 题目提交
     *
     * @param questionSubmissionAddRequest 题目提交请求
     * @param userId                用户id
     * @return {@link Long} 题目提交id
     */
    Boolean doQuestionSubmit(QuestionSubmissionAddRequest questionSubmissionAddRequest, Long userId);

    /**
     * 获取查询条件
     *
     * @param questionSubmissionQueryRequest 题目提交查询条件
     * @return {@link LambdaQueryWrapper <QuestionSubmit>} 查询条件
     */
    LambdaQueryWrapper<QuestionSubmission> getDetailedQueryWrapper(QuestionSubmissionQueryRequest questionSubmissionQueryRequest);

    /**
     * 获取用户某个题目的提交记录
     *
     * @param questionId 题目id
     * @param userId     用户id
     * @return {@link List< QuestionSubmission >} 题目提交列表
     */
    List<QuestionSubmission> listMyQuestionSubmitById(Long questionId, Long userId);

    /**
     * 获取查询条件
     *
     * @param userId 用户id
     * @return {@link LambdaQueryWrapper< QuestionSubmission >} 查询条件
     */
    LambdaQueryWrapper<QuestionSubmission> getUserQueryWrapper(Long userId);

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage 题目提交分页
     * @return {@link Page < DetailedQuestionSubmitVO >} 题目提交vo分页
     */
    Page<DetailedQuestionSubmissionVO> getDetailedQuestionSubmitVOPage(Page<QuestionSubmission> questionSubmitPage);

    /**
     * 分页获取简单题目提交封装
     *
     * @param questionSubmitPage 题目提交分页
     * @return {@link Page< DetailedQuestionSubmissionVO >} 题目提交vo分页
     */
    Page<QuestionSubmissionVO> getQuestionSubmitVOPage(Page<QuestionSubmission> questionSubmitPage);

    /**
     * 获取某个题目的提交记录列表
     *
     * @param questionSubmissionList 题目提交列表
     * @return {@link Page<  ViewQuestionSubmissionVO  >} 题目提交vo列表
     */
    List<ViewQuestionSubmissionVO> getViewQuestionSubmitVOList(List<QuestionSubmission> questionSubmissionList);
}
