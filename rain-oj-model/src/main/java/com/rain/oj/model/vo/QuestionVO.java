package com.rain.oj.model.vo;

import cn.hutool.json.JSONUtil;
import com.rain.oj.model.dto.question.JudgeConfig;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.enums.QuestionDifficultyEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目视图
 */
@Data
public class QuestionVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 编号
     */
    private Integer number;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 难度
     */
    private String difficulty;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

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
     * 创建用户 id
     */
    private Long userId;
    /**
     * 创建题目人的信息
     */
    private UserVO userVO;
    /**
     * 是否已点赞
     */
    private Boolean hasThumb;

    /**
     * 是否已收藏
     */
    private Boolean hasFavour;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    /**
     * 对象转包装类
     *
     * @param question 题目
     * @return {@link QuestionVO} 题目提交vo
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        Integer difficulty = question.getDifficulty();
        questionVO.setDifficulty(QuestionDifficultyEnum.getEnumByValue(difficulty).getDifficulty());
        List<String> tagList = JSONUtil.toList(question.getTags(), String.class);
        questionVO.setTags(tagList);
        String judgeConfig = question.getJudgeConfig();
        questionVO.setJudgeConfig(JSONUtil.toBean(judgeConfig, JudgeConfig.class));
        return questionVO;
    }
}