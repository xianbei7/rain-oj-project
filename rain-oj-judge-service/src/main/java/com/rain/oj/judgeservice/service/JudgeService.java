package com.rain.oj.judgeservice.service;

import com.rain.oj.model.entity.QuestionSubmit;
import com.rain.oj.model.judge.JudgeResult;
import com.rain.oj.model.judge.codesandbox.ExecuteCodeRequest;
import com.rain.oj.model.judge.codesandbox.ExecuteCodeResponse;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     *
     * @param questionSubmitId 题目提交id
     * @return {@link Boolean} 是否更新成功
     */
    Boolean doJudge(Long questionSubmitId);

    /**
     * 判题并更新数据库中题目提交
     *
     * @param executeCodeResponse 执行代码沙箱的结果
     * @return {@link Boolean} 是否更新成功
     */
    Boolean judgeAndUpdate(ExecuteCodeResponse executeCodeResponse);

    /**
     * 设置为判题失败
     *
     * @param executeId 执行代码沙箱id
     */
    void setJudgeFail(String executeId);
}
