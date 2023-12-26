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
 * Java判题策略
 */
public class JavaLanguageJudgeStrategy implements JudgeStrategy {

    /**
     * 执行判题
     *
     * @param judgeContext 判题上下文
     * @return {@link JudgeResult} 判题结果
     */
    @Override
    public JudgeResult doJudge(JudgeContext judgeContext) {

        JudgeResult judgeResult = new JudgeResult();
        String errorType = judgeContext.getErrorType();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        int correctCaseCount = 0;
        int totalCaseCount = judgeCaseList.size();
        if (JudgeResultEnum.needJudge(errorType)) {
            List<JudgeInfo> judgeInfoList = judgeContext.getJudgeInfo();
            List<String> outputList = judgeContext.getOutputList();
            Question question = judgeContext.getQuestion();
            String judgeConfigStr = question.getJudgeConfig();
            JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
            Integer needMemoryLimit = judgeConfig.getMemoryLimit();
            Integer needTimeLimit = judgeConfig.getTimeLimit();

            boolean isSetMessage = false;
            JudgeResultEnum judgeResultEnum = JudgeResultEnum.ACCEPTED;
            // 依次判断每一项输出和预期输出是否相等
            for (int i = 0; i < totalCaseCount; i++) {
                JudgeCase judgeCase = judgeCaseList.get(i);
                if (!judgeCase.getOutput().equals(outputList.get(i).trim())) {
                    if (!isSetMessage) {
                        judgeResultEnum = JudgeResultEnum.WRONG_ANSWER;
                        isSetMessage = true;
                    }
                } else {
                    JudgeInfo judgeInfo = judgeInfoList.get(i);
                    Long memory = judgeInfo.getMemory();
                    Long time = judgeInfo.getTime();
                    if (memory > needMemoryLimit) {
                        if (!isSetMessage) {
                            judgeResultEnum = JudgeResultEnum.MEMORY_LIMIT_EXCEEDED;
                            isSetMessage = true;
                        }
                    } else if (time > needTimeLimit + 10) {
                        if (!isSetMessage) {
                            judgeResultEnum = JudgeResultEnum.TIME_LIMIT_EXCEEDED;
                            isSetMessage = true;
                        }
                    } else {
                        correctCaseCount++;
                    }
                }
            }
            judgeResult.setType(judgeResultEnum.getType());
        } else {
            judgeResult.setType(errorType);
            judgeResult.setMessage(judgeContext.getErrorMessage());
        }
        judgeResult.setCorrectRate(correctCaseCount * 100 / totalCaseCount);

        return judgeResult;
    }
}
