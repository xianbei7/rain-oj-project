package com.rain.oj.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 题目更新请求
 */
@Data
public class QuestionSaveRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 编号
     */
    private Integer number;

    /**
     * 内容
     */
    private String content;

    /**
     * 难度
     */
    private String difficulty;

    /**
     * 类型
     */
    private String type;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

    private static final long serialVersionUID = 1L;
}