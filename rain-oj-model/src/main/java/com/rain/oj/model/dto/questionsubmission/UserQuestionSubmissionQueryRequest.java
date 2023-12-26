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
public class UserQuestionSubmissionQueryRequest extends PageRequest implements Serializable {

    private Long userId;

    private static final long serialVersionUID = 1L;
}