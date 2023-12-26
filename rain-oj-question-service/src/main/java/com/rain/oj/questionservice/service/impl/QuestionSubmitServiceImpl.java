package com.rain.oj.questionservice.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.feignclient.service.JudgeFeignClient;
import com.rain.oj.feignclient.service.UserFeignClient;
import com.rain.oj.model.dto.questionsubmission.QuestionSubmissionAddRequest;
import com.rain.oj.model.dto.questionsubmission.QuestionSubmissionQueryRequest;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.entity.QuestionSubmission;
import com.rain.oj.model.entity.User;
import com.rain.oj.model.enums.ProgrammingLanguageEnum;
import com.rain.oj.model.enums.QuestionDifficultyEnum;
import com.rain.oj.model.enums.QuestionSubmissionStatusEnum;
import com.rain.oj.model.judge.JudgeInfo;
import com.rain.oj.model.vo.DetailedQuestionSubmissionVO;
import com.rain.oj.model.vo.QuestionSubmissionVO;
import com.rain.oj.model.vo.ViewQuestionSubmissionVO;
import com.rain.oj.questionservice.mapper.QuestionSubmitMapper;
import com.rain.oj.questionservice.service.QuestionService;
import com.rain.oj.questionservice.service.QuestionSubmitService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RRateLimiter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 题目提交服务实现类
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmission>
        implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private RRateLimiter rateLimiter;

    /**
     * 题目提交
     *
     * @param questionSubmissionAddRequest 题目提交请求
     * @param userId                   用户id
     * @return {@link Boolean} 题目提交id
     */
    @Override
    public Boolean doQuestionSubmit(QuestionSubmissionAddRequest questionSubmissionAddRequest, Long userId) {
        // 校验编程语言是否合法
        String language = questionSubmissionAddRequest.getLanguage();
        ProgrammingLanguageEnum languageEnum = ProgrammingLanguageEnum.getEnumByLanguage(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        Long questionId = questionSubmissionAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 限流：限制用户在短时间内提交的次数
        boolean acquire = rateLimiter.tryAcquire();
        if (acquire) {
            // 每个用户串行提交题目
            QuestionSubmission questionSubmission = new QuestionSubmission();
            questionSubmission.setUserId(userId);
            questionSubmission.setQuestionId(questionId);
            questionSubmission.setCode(questionSubmissionAddRequest.getCode());
            questionSubmission.setLanguage(languageEnum.getLanguage());
            // 设置初始状态
            questionSubmission.setStatus(QuestionSubmissionStatusEnum.WAITING.getStatus());
            questionSubmission.setJudgeInfo("[]");
            boolean save = save(questionSubmission);
            if (!save) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
            }
            Long questionSubmitId = questionSubmission.getId();
            // 发送消息
            return judgeFeignClient.doJudge(questionSubmitId);
        } else {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "操作过于频繁！请稍后再试");
        }
    }

    /**
     * 获取查询条件
     *
     * @param questionSubmissionQueryRequest 题目提交查询条件
     * @return {@link LambdaQueryWrapper< QuestionSubmission >} 查询条件
     */
    @Override
    public LambdaQueryWrapper<QuestionSubmission> getDetailedQueryWrapper(QuestionSubmissionQueryRequest questionSubmissionQueryRequest) {
        LambdaQueryWrapper<QuestionSubmission> queryWrapper = new LambdaQueryWrapper<>();
        if (questionSubmissionQueryRequest == null) {
            return queryWrapper;
        }
        List<Long> questionIds = null;
        List<Long> userIds = null;
        String difficulty = questionSubmissionQueryRequest.getDifficulty();
        String language = questionSubmissionQueryRequest.getLanguage();
        String status = questionSubmissionQueryRequest.getStatus();
        String userSearchText = questionSubmissionQueryRequest.getUserSearchText();
        String questionSearchText = questionSubmissionQueryRequest.getQuestionSearchText();

        // 搜索字段：题目编号、用户名、用户id或用户名、编程语言、提交状态、
        if (!StringUtils.isAllBlank(difficulty, questionSearchText)) {
            QuestionDifficultyEnum difficultyEnum = QuestionDifficultyEnum.getEnumByText(difficulty);
            LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (difficultyEnum != null) {
                questionLambdaQueryWrapper.eq(Question::getDifficulty, difficultyEnum.getValue());
            }
            if (StringUtils.isNotBlank(questionSearchText)) {
                questionLambdaQueryWrapper
                        .like(Question::getNumber, questionSearchText)
                        .or()
                        .like(Question::getTitle, questionSearchText);
            }
            questionIds = questionService.list(questionLambdaQueryWrapper).stream().map(Question::getId).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(userSearchText)) {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper
                    .eq(User::getId, userSearchText)
                    .or()
                    .like(User::getUserName, userSearchText)
                    .or()
                    .like(User::getUserNumber, userSearchText);
            userIds = userFeignClient.list(userLambdaQueryWrapper).stream().map(User::getId).collect(Collectors.toList());
        }
        queryWrapper.eq(StringUtils.isNotBlank(language), QuestionSubmission::getLanguage, language);
        QuestionSubmissionStatusEnum submitStatusEnum = QuestionSubmissionStatusEnum.getEnumByDisplay(status);
        if (submitStatusEnum != null) {
            queryWrapper.eq(QuestionSubmission::getStatus, submitStatusEnum.getStatus());
        }
        queryWrapper.in(CollectionUtil.isNotEmpty(questionIds), QuestionSubmission::getQuestionId, questionIds);
        queryWrapper.in(CollectionUtil.isNotEmpty(userIds), QuestionSubmission::getUserId, userIds);
        queryWrapper.orderBy(true, false, QuestionSubmission::getCreateTime);
        return queryWrapper;
    }

    /**
     * 获取用户某个题目的提交记录
     *
     * @param questionId 题目id
     * @param userId     用户id
     * @return {@link List< QuestionSubmission >} 题目提交列表
     */
    @Override
    public List<QuestionSubmission> listMyQuestionSubmitById(Long questionId, Long userId) {
        LambdaQueryWrapper<QuestionSubmission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(QuestionSubmission::getQuestionId, questionId);
        lambdaQueryWrapper.eq(QuestionSubmission::getUserId, userId);
        lambdaQueryWrapper.orderBy(true, false, QuestionSubmission::getCreateTime);
        return list(lambdaQueryWrapper);
    }

    /**
     * 获取查询条件
     *
     * @param userId 用户id
     * @return {@link LambdaQueryWrapper< QuestionSubmission >} 查询条件
     */
    @Override
    public LambdaQueryWrapper<QuestionSubmission> getUserQueryWrapper(Long userId) {
        LambdaQueryWrapper<QuestionSubmission> queryWrapper = new LambdaQueryWrapper<>();
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求用户不能为空");
        }
        queryWrapper.eq(QuestionSubmission::getUserId, userId);
        queryWrapper.orderBy(true, false, QuestionSubmission::getCreateTime);
        return queryWrapper;
    }

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage 题目提交分页
     * @return {@link Page< DetailedQuestionSubmissionVO >} 题目提交vo分页
     */
    @Override
    public Page<DetailedQuestionSubmissionVO> getDetailedQuestionSubmitVOPage(Page<QuestionSubmission> questionSubmitPage) {
        List<QuestionSubmission> questionSubmissionList = questionSubmitPage.getRecords();
        Page<DetailedQuestionSubmissionVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmissionList)) {
            return questionSubmitVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionSubmissionList.stream().map(QuestionSubmission::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 关联查询题目信息
        Set<Long> questionIdSet = questionSubmissionList.stream().map(QuestionSubmission::getQuestionId).collect(Collectors.toSet());
        Map<Long, List<Question>> questionIdQuestionListMap = questionService.listByIds(questionIdSet).stream()
                .collect(Collectors.groupingBy(Question::getId));
        // 3. 填充信息
        List<DetailedQuestionSubmissionVO> detailedQuestionSubmissionVOList = questionSubmissionList.stream().map(questionSubmit -> {
            DetailedQuestionSubmissionVO detailedQuestionSubmissionVO = DetailedQuestionSubmissionVO.objToVo(questionSubmit);
            // 防止代码泄漏
            Long userId = questionSubmit.getUserId();
            Long questionId = questionSubmit.getQuestionId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            detailedQuestionSubmissionVO.setUserVO(userFeignClient.getUserVO(user));

            Question question = null;
            if (questionIdQuestionListMap.containsKey(questionId)) {
                question = questionIdQuestionListMap.get(questionId).get(0);
            }
            if (question != null) {
                detailedQuestionSubmissionVO.setQuestionId(questionId);
                detailedQuestionSubmissionVO.setQuestionNumber(question.getNumber());
                detailedQuestionSubmissionVO.setQuestionTitle(question.getTitle());
                QuestionDifficultyEnum questionDifficultyEnum = QuestionDifficultyEnum.getEnumByValue(question.getDifficulty());
                if (questionDifficultyEnum != null) {
                    detailedQuestionSubmissionVO.setQuestionDifficulty(questionDifficultyEnum.getDifficulty());
                }
            }
            return detailedQuestionSubmissionVO;
        }).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(detailedQuestionSubmissionVOList);
        return questionSubmitVOPage;
    }

    /**
     * 分页获取简单题目提交封装
     *
     * @param questionSubmitPage 题目提交分页
     * @return {@link Page< DetailedQuestionSubmissionVO >} 题目提交vo分页
     */
    @Override
    public Page<QuestionSubmissionVO> getQuestionSubmitVOPage(Page<QuestionSubmission> questionSubmitPage) {
        List<QuestionSubmission> questionSubmissionList = questionSubmitPage.getRecords();
        Page<QuestionSubmissionVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmissionList)) {
            return questionSubmitVOPage;
        }
        // 1. 关联查询题目信息
        Set<Long> questionIdSet = questionSubmissionList.stream().map(QuestionSubmission::getQuestionId).collect(Collectors.toSet());
        Map<Long, List<Question>> questionIdQuestionListMap = questionService.listByIds(questionIdSet).stream()
                .collect(Collectors.groupingBy(Question::getId));
        // 2. 填充信息
        List<QuestionSubmissionVO> questionSubmissionVOList = questionSubmissionList.stream().map(questionSubmit -> {
            QuestionSubmissionVO questionSubmissionVO = QuestionSubmissionVO.objToVo(questionSubmit);
            Long questionId = questionSubmit.getQuestionId();
            Question question = null;
            if (questionIdQuestionListMap.containsKey(questionId)) {
                question = questionIdQuestionListMap.get(questionId).get(0);
            }
            if (question != null) {
                questionSubmissionVO.setQuestionId(questionId);
                questionSubmissionVO.setQuestionNumber(question.getNumber());
                questionSubmissionVO.setQuestionTitle(question.getTitle());
            }
            return questionSubmissionVO;
        }).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmissionVOList);
        return questionSubmitVOPage;
    }

    /**
     * 获取某个题目的提交记录列表
     *
     * @param questionSubmissionList 题目提交列表
     * @return {@link Page< ViewQuestionSubmissionVO >} 题目提交vo列表
     */
    @Override
    public List<ViewQuestionSubmissionVO> getViewQuestionSubmitVOList(List<QuestionSubmission> questionSubmissionList) {
        if (CollectionUtils.isEmpty(questionSubmissionList)) {
            return null;
        }
        return questionSubmissionList.stream().map(questionSubmit -> {
            ViewQuestionSubmissionVO viewQuestionSubmissionVO = ViewQuestionSubmissionVO.objToVo(questionSubmit);
            long executeTime = 0L;
            long executeMemory = 0L;
            String judgeInfoStr = questionSubmit.getJudgeInfo();
            List<JudgeInfo> judgeInfoList = JSONUtil.toList(judgeInfoStr, JudgeInfo.class);
            for (JudgeInfo judgeInfo : judgeInfoList) {
                executeTime = Math.max(executeTime, judgeInfo.getTime() == null ? 0 : judgeInfo.getTime());
                executeMemory = Math.max(executeMemory, judgeInfo.getMemory() == null ? 0 : judgeInfo.getMemory());
            }
            viewQuestionSubmissionVO.setExecuteTime((int) executeTime);
            viewQuestionSubmissionVO.setExecuteMemory((int) (executeMemory / 1024));
            return viewQuestionSubmissionVO;
        }).collect(Collectors.toList());
    }
}




