package com.rain.oj.questionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rain.oj.model.dto.question.QuestionQueryRequest;
import com.rain.oj.model.dto.question.QuestionSaveRequest;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.entity.User;
import com.rain.oj.model.vo.DoQuestionVO;
import com.rain.oj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

public interface QuestionService extends IService<Question> {
    /**
     * 校验题目是否合法
     *
     * @param question 题目
     * @param add      是否新增
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest 题目查询请求
     * @return {@link LambdaQueryWrapper <Question>} 题目查询条件
     */
    LambdaQueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question 题目
     * @param request  Http请求
     * @return {@link DoQuestionVO} 做题vo
     */
    DoQuestionVO getQuestionVO(Question question, HttpServletRequest request) ;

    /**
     * 分页获取题目封装
     *
     * @param questionPage 题目分页
     * @param request      Http请求
     * @return {@link Page < QuestionVO >} 题目vo分页
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    /**
     * 保存题目
     *
     * @param questionSaveRequest 题目保存请求
     * @param loginUser           登录用户
     * @param add                 是否新增
     * @return {@link Boolean} 是否保存成功
     */
    Boolean saveQuestion(QuestionSaveRequest questionSaveRequest, User loginUser, boolean add);
}
