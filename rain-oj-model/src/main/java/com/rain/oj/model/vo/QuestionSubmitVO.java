package com.rain.oj.model.vo;

import cn.hutool.json.JSONUtil;
import com.rain.oj.model.entity.QuestionSubmit;
import com.rain.oj.model.enums.QuestionSubmitStatusEnum;
import com.rain.oj.model.judge.JudgeResult;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交视图
 */
@Data
public class QuestionSubmitVO implements Serializable {

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

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2  成功、3 - 失败）
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
     * @param questionSubmit 题目提交
     * @return {@link QuestionSubmitVO} 题目提交vo
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        String judgeResult = questionSubmit.getJudgeResult();
        questionSubmitVO.setJudgeResult(JSONUtil.toBean(judgeResult, JudgeResult.class));
        Integer status = questionSubmit.getStatus();
        questionSubmitVO.setStatus(QuestionSubmitStatusEnum.getEnumByValue(status).getStatus());
        return questionSubmitVO;
    }

}