package com.rain.oj.judgeservice.strategy;


import com.rain.oj.model.judge.JudgeResult;

public interface JudgeStrategy {

    /**
     * 执行判题
     *
     * @param judgeContext 判题上下文
     * @return {@link JudgeResult} 判题结果
     */
    JudgeResult doJudge(JudgeContext judgeContext);
}
