package com.rain.oj.model.dto.questionsubmission;

import com.rain.oj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 题目提交请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmissionQueryRequest extends PageRequest implements Serializable {

    /**
     * 题目难度
     */
    private String difficulty;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private String status;

    /**
     * 用户搜索词
     */
    private String userSearchText;

    /**
     * 题目搜索词
     */
    private String questionSearchText;

    private static final long serialVersionUID = 1L;
}