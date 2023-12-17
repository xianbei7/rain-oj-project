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
 * 查看题目提交视图
 */
@Data
public class ViewQuestionSubmitVO implements Serializable {

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
     * @return {@link ViewQuestionSubmitVO} 题目提交vo
     */
    public static ViewQuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        ViewQuestionSubmitVO questionSubmitVO = new ViewQuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        String judgeResult = questionSubmit.getJudgeResult();
        questionSubmitVO.setJudgeResult(JSONUtil.toBean(judgeResult, JudgeResult.class));
        Integer status = questionSubmit.getStatus();
        questionSubmitVO.setStatus(QuestionSubmitStatusEnum.getEnumByValue(status).getStatus());
        return questionSubmitVO;
    }

}