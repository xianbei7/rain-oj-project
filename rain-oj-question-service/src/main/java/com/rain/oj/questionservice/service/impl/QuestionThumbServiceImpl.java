package com.rain.oj.questionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.entity.QuestionThumb;
import com.rain.oj.model.entity.User;
import com.rain.oj.questionservice.mapper.QuestionThumbMapper;
import com.rain.oj.questionservice.service.QuestionService;
import com.rain.oj.questionservice.service.QuestionThumbService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 题目点赞服务实现
 */
@Service
public class QuestionThumbServiceImpl extends ServiceImpl<QuestionThumbMapper, QuestionThumb>
        implements QuestionThumbService {

    @Resource
    private QuestionService questionService;

    /**
     * 点赞
     *
     * @param questionId 题目id
     * @param loginUser  登录用户
     * @return {@link int} 点赞结果
     */
    @Override
    public int doQuestionThumb(long questionId, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        QuestionThumbService questionThumbService = (QuestionThumbService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return questionThumbService.doQuestionThumbInner(userId, questionId);
        }
    }

    /**
     * 题目点赞（内部服务）
     *
     * @param userId     用户id
     * @param questionId 题目id
     * @return {@link int} 点赞结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionThumbInner(long userId, long questionId) {
        QuestionThumb questionThumb = new QuestionThumb();
        questionThumb.setUserId(userId);
        questionThumb.setQuestionId(questionId);
        LambdaQueryWrapper<QuestionThumb> thumbQueryWrapper = new LambdaQueryWrapper<>(questionThumb);
        QuestionThumb oldQuestionThumb = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldQuestionThumb != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = questionService.update()
                        .eq("id", questionId)
                        .gt("thumb_num", 0)
                        .setSql("thumb_num = thumb_num - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(questionThumb);
            if (result) {
                // 点赞数 + 1
                result = questionService.update()
                        .eq("id", questionId)
                        .setSql("thumb_num = thumb_num + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

}




