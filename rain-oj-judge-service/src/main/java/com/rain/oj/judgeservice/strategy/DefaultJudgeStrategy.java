package com.rain.oj.judgeservice.strategy;

import cn.hutool.json.JSONUtil;
import com.rain.oj.model.dto.question.JudgeCase;
import com.rain.oj.model.dto.question.JudgeConfig;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.enums.JudgeResultEnum;
import com.rain.oj.model.judge.JudgeInfo;
import com.rain.oj.model.judge.JudgeResult;

import java.util.List;

/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy {

    /**
     * 执行判题
     *
     * @param judgeContext 判题上下文
     * @return {@link JudgeResult} 判题结果
     */
    @Override
    public JudgeResult doJudge(JudgeContext judgeContext) {
        JudgeResult judgeResult = new JudgeResult();
        List<JudgeInfo> judgeInfoList = judgeContext.getJudgeInfo();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Integer needMemoryLimit = judgeConfig.getMemoryLimit();
        Integer needTimeLimit = judgeConfig.getTimeLimit();

        boolean setMessage = false;
        JudgeResultEnum judgeResultEnum = JudgeResultEnum.ACCEPTED;
        int totalCaseCount = judgeCaseList.size();
        int correctCaseCount = 0;
        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < totalCaseCount; i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                if (!setMessage) {
                    judgeResultEnum = JudgeResultEnum.WRONG_ANSWER;
                    setMessage = true;
                }
            } else {
                JudgeInfo judgeInfo = judgeInfoList.get(i);
                Long memory = judgeInfo.getMemory();
                Long time = judgeInfo.getTime();
                if (memory > needMemoryLimit) {
                    if (!setMessage) {
                        judgeResultEnum = JudgeResultEnum.MEMORY_LIMIT_EXCEEDED;
                        setMessage = true;
                    }
                } else if (time > needTimeLimit) {
                    if (!setMessage) {
                        judgeResultEnum = JudgeResultEnum.TIME_LIMIT_EXCEEDED;
                        setMessage = true;
                    }
                } else {
                    correctCaseCount++;
                }
            }
        }
        judgeResult.setCorrectRate(correctCaseCount*100 / totalCaseCount);
        judgeResult.setMessage(judgeResultEnum.getType());
        return judgeResult;
    }
}
