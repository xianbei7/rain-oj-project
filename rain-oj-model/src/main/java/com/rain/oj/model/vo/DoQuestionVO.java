package com.rain.oj.model.vo;

import cn.hutool.json.JSONUtil;
import com.rain.oj.model.dto.question.JudgeConfig;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.enums.QuestionTypeEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 做题题目视图（脱敏）
 */
@Data
public class DoQuestionVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型
     */
    private String type;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 判题配置（json 数组）
     */
    private JudgeConfig judgeConfig;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 是否已点赞
     */
    private Boolean hasThumb;

    /**
     * 是否已收藏
     */
    private Boolean hasFavour;

    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param doQuestionVO 做题vo
     * @return {@link }
     */
    public static Question voToObj(DoQuestionVO doQuestionVO) {
        if (doQuestionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(doQuestionVO, question);
        List<String> tagList = doQuestionVO.getTags();
        JudgeConfig judgeConfig = doQuestionVO.getJudgeConfig();
        if (tagList != null) {
            question.setTags(JSONUtil.toJsonStr(tagList));
        }
        if (judgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question 题目
     * @return {@link DoQuestionVO}
     */
    public static DoQuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        DoQuestionVO questionVO = new DoQuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        Integer questionType = question.getType();
        questionVO.setType(QuestionTypeEnum.getEnumByValue(questionType).getType());
        List<String> tagList = JSONUtil.toList(question.getTags(), String.class);
        questionVO.setTags(tagList);
        String judgeConfig = question.getJudgeConfig();
        questionVO.setJudgeConfig(JSONUtil.toBean(judgeConfig, JudgeConfig.class));
        return questionVO;
    }
}