package com.rain.oj.questionservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rain.oj.model.entity.QuestionTemplate;

/**
 * 题目模板服务
 */
public interface QuestionTemplateService extends IService<QuestionTemplate> {

    /**
     * 获取题目模板
     *
     * @param questionId 题目id
     * @param language   语言
     * @return {@link String}题目模板
     */
    String getCodeTemplate(Long questionId, String language);
}
