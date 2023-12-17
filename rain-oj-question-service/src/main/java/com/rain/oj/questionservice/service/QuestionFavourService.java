package com.rain.oj.questionservice.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rain.oj.model.bo.QuestionFavourBO;
import com.rain.oj.model.entity.QuestionFavour;
import com.rain.oj.model.entity.User;
import com.rain.oj.model.vo.QuestionFavourVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 题目收藏服务
 */
public interface QuestionFavourService extends IService<QuestionFavour> {

    /**
     * 题目收藏
     *
     * @param questionId 题目id
     * @param loginUser  登录用户
     * @return {@link int} 点赞状态
     */
    int doQuestionFavour(long questionId, User loginUser);

    /**
     * 分页查询用户收藏
     *
     * @param page   用户收藏分页
     * @param userId 用户id
     * @return {@link Page<QuestionFavourBO>} 题目收藏bo分页
     */
    Page<QuestionFavourBO> listFavourQuestionByPage(IPage<QuestionFavourBO> page, Long userId);

    /**
     * 分页获取用户自己收藏的题目列表
     *
     * @param page         用户收藏分页
     * @param queryWrapper 查询条件
     * @param favourUserId 收藏用户id
     * @return @return {@link Page<QuestionFavourVO>} 题目收藏vo分页列表
     */
    Page<QuestionFavourVO> listMyFavourQuestionByPage(IPage<QuestionFavourVO> page, Wrapper<QuestionFavourVO> queryWrapper,
                                                      long favourUserId);

    /**
     * 操作数据库点赞状态（封装了事务的方法）
     *
     * @param userId     用户id
     * @param questionId 题目id
     * @return {@link int} 点赞状态
     */
    int doQuestionFavourInner(long userId, long questionId);

    /**
     * 获取用户收藏bo分页
     *
     * @param favourQuestionPage 题目收藏分页
     * @param request            Http请求
     * @return {@link Page<QuestionFavourVO>} 题目收藏vo分页
     */
    Page<QuestionFavourVO> getFavourQuestionVOPage(Page<QuestionFavourBO> favourQuestionPage, HttpServletRequest request);

}
