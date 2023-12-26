package com.rain.oj.questionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.constant.CodeTemplateConstant;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.entity.QuestionTemplate;
import com.rain.oj.model.enums.ProgrammingLanguageEnum;
import com.rain.oj.model.enums.QuestionModeEnum;
import com.rain.oj.questionservice.mapper.QuestionTemplateMapper;
import com.rain.oj.questionservice.service.QuestionService;
import com.rain.oj.questionservice.service.QuestionTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 题目模板服务实现
 */
@Service
public class QuestionTemplateServiceImpl extends ServiceImpl<QuestionTemplateMapper, QuestionTemplate>
        implements QuestionTemplateService {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取题目模板
     *
     * @param questionId 题目id
     * @param language   语言
     * @return {@link String}题目模板
     */
    @Override
    public String getCodeTemplate(Long questionId, String language) {
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目不存在");
        }
        ProgrammingLanguageEnum languageEnum = ProgrammingLanguageEnum.getEnumByLanguage(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        Integer mode = question.getMode();
        QuestionModeEnum questionModeEnum = QuestionModeEnum.getEnumByValue(mode);
        if (questionModeEnum.equals(QuestionModeEnum.ACM)) {
            String codeTemplate = stringRedisTemplate.opsForValue().get(CodeTemplateConstant.CODE_TEMPLATE_KEY + languageEnum.getLanguage());
            if (StringUtils.isNotBlank(codeTemplate)) {
                return codeTemplate;

            }
            StringBuilder template = new StringBuilder();
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("/codetemplate/" + languageEnum.getLanguage());
            try (InputStream inputStream = resource.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    template.append(line).append("\n");
                }
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "读取代码模板失败");
            }
            return template.toString();
        } else if (questionModeEnum.equals(QuestionModeEnum.CORE)) {
            LambdaQueryWrapper<QuestionTemplate> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(QuestionTemplate::getQuestionId, questionId);
            queryWrapper.eq(QuestionTemplate::getLanguage, languageEnum.getLanguage());
            QuestionTemplate questionTemplate = getOne(queryWrapper);
            return questionTemplate.getCodeTemplate();
        } else {
            return null;
        }
    }
}




