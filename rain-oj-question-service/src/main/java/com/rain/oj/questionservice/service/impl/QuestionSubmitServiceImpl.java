package com.rain.oj.questionservice.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.constant.JudgeConstant;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.rain.oj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.rain.oj.model.entity.Question;
import com.rain.oj.model.entity.QuestionSubmit;
import com.rain.oj.model.entity.User;
import com.rain.oj.model.enums.ProgrammingLanguageEnum;
import com.rain.oj.model.enums.QuestionDifficultyEnum;
import com.rain.oj.model.enums.QuestionSubmitStatusEnum;
import com.rain.oj.model.judge.JudgeInfo;
import com.rain.oj.model.vo.DetailedQuestionSubmitVO;
import com.rain.oj.model.vo.QuestionSubmitVO;
import com.rain.oj.model.vo.ViewQuestionSubmitVO;
import com.rain.oj.questionservice.mapper.QuestionSubmitMapper;
import com.rain.oj.feignclient.service.*;
import com.rain.oj.questionservice.service.QuestionService;
import com.rain.oj.questionservice.service.QuestionSubmitService;
import org.apache.commons.lang3.StringUtils;
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
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private JudgeFeignClient judgeFeignClient;

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest 题目提交请求
     * @param loginUser                登录用户
     * @return {@link Long} 题目提交id
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        ProgrammingLanguageEnum languageEnum = ProgrammingLanguageEnum.getEnumByLanguage(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        // todo 限流：限制用户在短时间内提交的次数(guava工具库)
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(languageEnum.getLanguage());
        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("[]");
        boolean save = save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        Long questionSubmitId = questionSubmit.getId();
        // 发送消息
        judgeFeignClient.doJudge(questionSubmitId);
        return questionSubmitId;
    }

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest 题目提交查询条件
     * @return {@link LambdaQueryWrapper<QuestionSubmit>} 查询条件
     */
    @Override
    public LambdaQueryWrapper<QuestionSubmit> getDetailedQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        LambdaQueryWrapper<QuestionSubmit> queryWrapper = new LambdaQueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        List<Long> questionIds = null;
        List<Long> userIds = null;
        String difficulty = questionSubmitQueryRequest.getDifficulty();
        String language = questionSubmitQueryRequest.getLanguage();
        String status = questionSubmitQueryRequest.getStatus();
        String userSearchText = questionSubmitQueryRequest.getUserSearchText();
        String questionSearchText = questionSubmitQueryRequest.getQuestionSearchText();

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
        queryWrapper.eq(StringUtils.isNotBlank(language), QuestionSubmit::getLanguage, language);
        QuestionSubmitStatusEnum submitStatusEnum = QuestionSubmitStatusEnum.getEnumByStatus(status);
        if (submitStatusEnum != null) {
            queryWrapper.eq(QuestionSubmit::getStatus, submitStatusEnum.getValue());
        }
        queryWrapper.in(CollectionUtil.isNotEmpty(questionIds), QuestionSubmit::getQuestionId, questionIds);
        queryWrapper.in(CollectionUtil.isNotEmpty(userIds), QuestionSubmit::getUserId, userIds);
        queryWrapper.orderBy(true, false, QuestionSubmit::getCreateTime);
        return queryWrapper;
    }

    /**
     * 获取用户某个题目的提交记录
     *
     * @param questionId 题目id
     * @param userId     用户id
     * @return {@link List<QuestionSubmit>} 题目提交列表
     */
    @Override
    public List<QuestionSubmit> listMyQuestionSubmitById(Long questionId, Long userId) {
        LambdaQueryWrapper<QuestionSubmit> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(QuestionSubmit::getQuestionId, questionId);
        lambdaQueryWrapper.eq(QuestionSubmit::getUserId, userId);
        lambdaQueryWrapper.orderBy(true, false, QuestionSubmit::getCreateTime);
        return list(lambdaQueryWrapper);
    }

    /**
     * 获取查询条件
     *
     * @param userId 用户id
     * @return {@link LambdaQueryWrapper<QuestionSubmit>} 查询条件
     */
    @Override
    public LambdaQueryWrapper<QuestionSubmit> getUserQueryWrapper(Long userId) {
        LambdaQueryWrapper<QuestionSubmit> queryWrapper = new LambdaQueryWrapper<>();
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求用户不能为空");
        }
        queryWrapper.eq(QuestionSubmit::getUserId, userId);
        queryWrapper.orderBy(true, false, QuestionSubmit::getCreateTime);
        return queryWrapper;
    }

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage 题目提交分页
     * @return {@link Page<DetailedQuestionSubmitVO>} 题目提交vo分页
     */
    @Override
    public Page<DetailedQuestionSubmitVO> getDetailedQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<DetailedQuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionSubmitList.stream().map(QuestionSubmit::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 关联查询题目信息
        Set<Long> questionIdSet = questionSubmitList.stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
        Map<Long, List<Question>> questionIdQuestionListMap = questionService.listByIds(questionIdSet).stream()
                .collect(Collectors.groupingBy(Question::getId));
        // 3. 填充信息
        List<DetailedQuestionSubmitVO> detailedQuestionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> {
            DetailedQuestionSubmitVO detailedQuestionSubmitVO = DetailedQuestionSubmitVO.objToVo(questionSubmit);
            // 防止代码泄漏
            Long userId = questionSubmit.getUserId();
            Long questionId = questionSubmit.getQuestionId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            detailedQuestionSubmitVO.setUserVO(userFeignClient.getUserVO(user));

            Question question = null;
            if (questionIdQuestionListMap.containsKey(questionId)) {
                question = questionIdQuestionListMap.get(questionId).get(0);
            }
            if (question != null) {
                detailedQuestionSubmitVO.setQuestionId(questionId);
                detailedQuestionSubmitVO.setQuestionNumber(question.getNumber());
                detailedQuestionSubmitVO.setQuestionTitle(question.getTitle());
                QuestionDifficultyEnum questionDifficultyEnum = QuestionDifficultyEnum.getEnumByValue(question.getDifficulty());
                if (questionDifficultyEnum != null) {
                    detailedQuestionSubmitVO.setQuestionDifficulty(questionDifficultyEnum.getDifficulty());
                }
            }
            return detailedQuestionSubmitVO;
        }).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(detailedQuestionSubmitVOList);
        return questionSubmitVOPage;
    }

    /**
     * 分页获取简单题目提交封装
     *
     * @param questionSubmitPage 题目提交分页
     * @return {@link Page<DetailedQuestionSubmitVO>} 题目提交vo分页
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        // 1. 关联查询题目信息
        Set<Long> questionIdSet = questionSubmitList.stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
        Map<Long, List<Question>> questionIdQuestionListMap = questionService.listByIds(questionIdSet).stream()
                .collect(Collectors.groupingBy(Question::getId));
        // 2. 填充信息
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> {
            QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
            Long questionId = questionSubmit.getQuestionId();
            Question question = null;
            if (questionIdQuestionListMap.containsKey(questionId)) {
                question = questionIdQuestionListMap.get(questionId).get(0);
            }
            if (question != null) {
                questionSubmitVO.setQuestionId(questionId);
                questionSubmitVO.setQuestionNumber(question.getNumber());
                questionSubmitVO.setQuestionTitle(question.getTitle());
            }
            return questionSubmitVO;
        }).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

    /**
     * 获取某个题目的提交记录列表
     *
     * @param questionSubmitList 题目提交列表
     * @return {@link Page<ViewQuestionSubmitVO>} 题目提交vo列表
     */
    @Override
    public List<ViewQuestionSubmitVO> getViewQuestionSubmitVOList(List<QuestionSubmit> questionSubmitList) {
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return null;
        }
        return questionSubmitList.stream().map(questionSubmit -> {
            ViewQuestionSubmitVO viewQuestionSubmitVO = ViewQuestionSubmitVO.objToVo(questionSubmit);
            long executeTime = 0L;
            long executeMemory = 0L;
            String judgeInfoStr = questionSubmit.getJudgeInfo();
            List<JudgeInfo> judgeInfoList = JSONUtil.toList(judgeInfoStr, JudgeInfo.class);
            for (JudgeInfo judgeInfo : judgeInfoList) {
                executeTime = Math.max(executeTime, judgeInfo.getTime());
                executeMemory = Math.max(executeMemory, judgeInfo.getMemory());
            }
            viewQuestionSubmitVO.setExecuteTime((int) executeTime);
            viewQuestionSubmitVO.setExecuteMemory((int) (executeMemory / 1024));
            return viewQuestionSubmitVO;
        }).collect(Collectors.toList());
    }
}




