package com.rain.oj.questionservice.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rain.oj.model.bo.QuestionFavourBO;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.entity.QuestionFavour;
import org.apache.ibatis.annotations.Param;

/**
 * 题目收藏数据库操作
 */
public interface QuestionFavourMapper extends BaseMapper<QuestionFavour> {

    /**
     * 分页查询用户收藏题目列表
     *
     * @param page
     * @param userId
     * @return
     */
    Page<QuestionFavourBO> listFavourQuestionByPage(IPage<QuestionFavourBO> page, @Param("userId") Long userId);

    /**
     * 分页查询用户自己的收藏题目列表
     *
     * @param page
     * @param queryWrapper
     * @param favourUserId
     * @return
     */
    Page<Question> listMyFavourQuestionByPage(IPage<Question> page, @Param(Constants.WRAPPER) Wrapper<Question> queryWrapper,
                                              long favourUserId);

}




