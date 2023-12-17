package com.rain.oj.questionservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rain.oj.model.entity.QuestionThumb;
import com.rain.oj.model.entity.User;

/**
 * 题目点赞服务
 */
public interface QuestionThumbService extends IService<QuestionThumb> {

    /**
     * 点赞
     *
     * @param questionId 题目id
     * @param loginUser 登录用户
     * @return {@link int} 点赞结果
     */
    int doQuestionThumb(long questionId, User loginUser);

    /**
     * 题目点赞（内部服务）
     *
     * @param userId 用户id
     * @param questionId 题目id
     * @return {@link int} 点赞结果
     */
    int doQuestionThumbInner(long userId, long questionId);
}
