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
 * 查看题目提交视图
 */
@Data
public class ViewQuestionSubmissionVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户提交代码
     */
    private String code;

    /**
     * 执行用时
     */
    private Integer executeTime;

    /**
     * 消耗内存
     */
    private Integer executeMemory;

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
     * @return {@link ViewQuestionSubmissionVO} 题目提交vo
     */
    public static ViewQuestionSubmissionVO objToVo(QuestionSubmission questionSubmission) {
        if (questionSubmission == null) {
            return null;
        }
        ViewQuestionSubmissionVO questionSubmitVO = new ViewQuestionSubmissionVO();
        BeanUtils.copyProperties(questionSubmission, questionSubmitVO);
        String judgeResult = questionSubmission.getJudgeResult();
        questionSubmitVO.setJudgeResult(JSONUtil.toBean(judgeResult, JudgeResult.class));
        String status = questionSubmission.getStatus();
        questionSubmitVO.setStatusDisplay(QuestionSubmissionStatusEnum.getEnumByStatus(status).getDisplay());
        return questionSubmitVO;
    }

}