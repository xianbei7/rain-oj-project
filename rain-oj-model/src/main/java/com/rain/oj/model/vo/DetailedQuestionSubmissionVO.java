package com.rain.oj.model.vo;

import cn.hutool.json.JSONUtil;
import com.rain.oj.model.judge.JudgeResult;
import com.rain.oj.model.entity.QuestionSubmission;
import com.rain.oj.model.enums.QuestionSubmissionStatusEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交详细视图
 */
@Data
public class DetailedQuestionSubmissionVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 题目编号
     */
    private Integer questionNumber;

    /**
     * 题目标题
     */
    private String questionTitle;

    /**
     * 题目难度
     */
    private String questionDifficulty;

    /**
     * 提交用户VO
     */
    private UserVO userVO;


    /**
     * 编程语言
     */
    private String language;

    /**
     * 判题结果
     */
    private JudgeResult judgeResult;

    private String statusDisplay;

    /**
     * 判题状态（waiting - 待判题、running - 判题中、ac - 通过、fail - 失败）
     */
    private String status;

    /**
     * 提交时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    /**
     * 对象转包装类
     *
     * @param questionSubmission 题目提交
     * @return {@link DetailedQuestionSubmissionVO} 题目提交vo
     */
    public static DetailedQuestionSubmissionVO objToVo(QuestionSubmission questionSubmission) {
        if (questionSubmission == null) {
            return null;
        }
        DetailedQuestionSubmissionVO detailedQuestionSubmissionVO = new DetailedQuestionSubmissionVO();
        BeanUtils.copyProperties(questionSubmission, detailedQuestionSubmissionVO);
        String judgeResult = questionSubmission.getJudgeResult();
        detailedQuestionSubmissionVO.setJudgeResult(JSONUtil.toBean(judgeResult, JudgeResult.class));
        String status = questionSubmission.getStatus();
        detailedQuestionSubmissionVO.setStatusDisplay(QuestionSubmissionStatusEnum.getEnumByStatus(status).getDisplay());
        return detailedQuestionSubmissionVO;
    }

}