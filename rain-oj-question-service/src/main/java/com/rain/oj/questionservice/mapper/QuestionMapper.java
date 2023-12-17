package com.rain.oj.questionservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rain.oj.model.entity.Question;

/**
* 题目数据库操作
*/
public interface QuestionMapper extends BaseMapper<Question> {
    Integer getByNumber(Integer number);
}




