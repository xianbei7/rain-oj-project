package com.rain.oj.model.vo;

import cn.hutool.json.JSONUtil;
import com.rain.oj.model.entity.QuestionSubmission;
import com.rain.oj.model.enums.QuestionSubmissionStatusEnum;
import com.rain.oj.model.judge.JudgeResult;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交视图
 */
@Data
public class QuestionSubmissionVO implements Serializable {

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
     * @return {@link QuestionSubmissionVO} 题目提交vo
     */
    public static QuestionSubmissionVO objToVo(QuestionSubmission questionSubmission) {
        if (questionSubmission == null) {
            return null;
        }
        QuestionSubmissionVO questionSubmissionVO = new QuestionSubmissionVO();
        BeanUtils.copyProperties(questionSubmission, questionSubmissionVO);
        String judgeResult = questionSubmission.getJudgeResult();
        questionSubmissionVO.setJudgeResult(JSONUtil.toBean(judgeResult, JudgeResult.class));
        String status = questionSubmission.getStatus();
        questionSubmissionVO.setStatusDisplay(QuestionSubmissionStatusEnum.getEnumByStatus(status).getDisplay());
        return questionSubmissionVO;
    }

}