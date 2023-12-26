package com.rain.oj.judgeservice.strategy;

import com.rain.oj.model.dto.question.JudgeCase;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.judge.JudgeInfo;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private String language;

    private Long questionSubmitId;

    private List<JudgeInfo> judgeInfo;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private String errorType;

    private String errorMessage;

}
