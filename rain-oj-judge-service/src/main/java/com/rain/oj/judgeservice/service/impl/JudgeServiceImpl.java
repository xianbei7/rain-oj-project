package com.rain.oj.judgeservice.service.impl;

import cn.hutool.json.JSONUtil;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.constant.JudgeConstant;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.judgeservice.codesandbox.CodeSandboxTemplate;
import com.rain.oj.judgeservice.strategy.JudgeContext;
import com.rain.oj.judgeservice.strategy.JudgeManager;
import com.rain.oj.judgeservice.service.JudgeService;
import com.rain.oj.model.dto.question.JudgeCase;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.entity.QuestionSubmission;
import com.rain.oj.model.enums.JudgeResultEnum;
import com.rain.oj.model.enums.QuestionSubmissionStatusEnum;
import com.rain.oj.model.judge.JudgeInfo;
import com.rain.oj.model.judge.JudgeResult;
import com.rain.oj.model.judge.codesandbox.ExecuteCodeRequest;
import com.rain.oj.model.judge.codesandbox.ExecuteCodeResponse;
import com.rain.oj.feignclient.service.QuestionFeignClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 判题服务实现类
 */
@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private JudgeManager judgeManager;
    @Resource
    private CodeSandboxTemplate codeSandboxTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 判题
     *
     * @param questionSubmitId 题目提交id
     * @return {@link Boolean}是否更新成功
     */
    @Override
    public Boolean doJudge(Long questionSubmitId) {

        QuestionSubmission questionSubmission = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmission == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmission.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        if (!questionSubmission.getStatus().equals(QuestionSubmissionStatusEnum.WAITING.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题");
        }
        // todo 可扩展：分布式锁
        QuestionSubmission questionSubmissionUpdate = new QuestionSubmission();
        questionSubmissionUpdate.setId(questionSubmitId);
        questionSubmissionUpdate.setStatus(QuestionSubmissionStatusEnum.RUNNING.getStatus());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmissionUpdate).getData();
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目状态更新失败");
        }
        String language = questionSubmission.getLanguage();
        String code = questionSubmission.getCode();
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        UUID executeId = UUID.randomUUID();
        // 调用代码沙箱
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .executeId(executeId.toString())
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setLanguage(language);
        judgeContext.setQuestionSubmitId(questionSubmitId);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        String judgeContextJson = JSONUtil.toJsonStr(judgeContext);
        stringRedisTemplate.opsForValue().set(JudgeConstant.JUDGE_CONTEXT_KEY + executeId, judgeContextJson, JudgeConstant.JUDGE_CONTEXT_EXPIRE_TIME, TimeUnit.MINUTES);
        return codeSandboxTemplate.executeCode(executeCodeRequest);
    }

    /**
     * 判题并更新数据库中题目提交
     *
     * @param executeCodeResponse 执行代码沙箱的结果
     * @return {@link Boolean} 是否更新成功
     */
    public Boolean judgeAndUpdate(ExecuteCodeResponse executeCodeResponse) {
        // 根据沙箱的执行结果。设置题目的判题状态，是否运行成功
        String executeId = executeCodeResponse.getExecuteId();
        String judgeContextJson = stringRedisTemplate.opsForValue().get(JudgeConstant.JUDGE_CONTEXT_KEY + executeId);
        if (StringUtils.isBlank(judgeContextJson)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "判题上下文不存在");
        }
        JudgeContext judgeContext = JSONUtil.toBean(judgeContextJson, JudgeContext.class);
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfoList());
        judgeContext.setErrorType(executeCodeResponse.getErrorType());
        judgeContext.setErrorMessage(executeCodeResponse.getErrorMessage());
        judgeContext.setOutputList(executeCodeResponse.getOutputList());

        JudgeResult judgeResult = judgeManager.doJudge(judgeContext);
        Boolean isAllSuccess = executeCodeResponse.getIsAllSuccess();
        List<JudgeInfo> judgeInfoList = executeCodeResponse.getJudgeInfoList();

        QuestionSubmission questionSubmission = new QuestionSubmission();
        // 根据题目的判题结果，设置题目的状态
        String status = isAllSuccess && judgeResult.getCorrectRate().equals(100) ? QuestionSubmissionStatusEnum.ACCEPTED.getStatus() : QuestionSubmissionStatusEnum.FAIL.getStatus();
        questionSubmission.setStatus(status);

        // 修改数据库中的判题结果
        questionSubmission.setId(judgeContext.getQuestionSubmitId());
        String jsonStr = JSONUtil.toJsonStr(judgeResult);
        questionSubmission.setJudgeResult(jsonStr);
        questionSubmission.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoList));
        return questionFeignClient.updateQuestionSubmitById(questionSubmission).getData();
    }

    /**
     * 设置为判题失败
     *
     * @param executeId 执行代码沙箱id
     */
    @Override
    public void setJudgeFail(String executeId) {
        String judgeContextJson = stringRedisTemplate.opsForValue().get(JudgeConstant.JUDGE_CONTEXT_KEY + executeId);
        JudgeContext judgeContext = JSONUtil.toBean(judgeContextJson, JudgeContext.class);
        QuestionSubmission questionSubmission = new QuestionSubmission();
        questionSubmission.setId(judgeContext.getQuestionSubmitId());
        // 根据题目的判题结果，设置题目的状态
        questionSubmission.setStatus(QuestionSubmissionStatusEnum.FAIL.getStatus());
        // 修改数据库中的判题结果
        JudgeResult judgeResult = new JudgeResult();
        judgeResult.setType(JudgeResultEnum.JUDGE_FAIL.getType());
        judgeResult.setMessage("系统繁忙！请稍后再试吧");
        judgeResult.setCorrectRate(0);
        String judgeResultJson = JSONUtil.toJsonStr(judgeResult);
        questionSubmission.setJudgeResult(judgeResultJson);
        questionFeignClient.updateQuestionSubmitById(questionSubmission);
    }
}