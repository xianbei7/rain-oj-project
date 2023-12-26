package com.rain.oj.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交实体类
 */
@TableName(value = "question_submission")
@Data
public class QuestionSubmission implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 题目 id
     */
    @TableField(value = "question_id")
    private Long questionId;

    /**
     * 提交用户 id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息（json 数组）
     */
    @TableField(value = "judge_info")
    private String judgeInfo;

    /**
     * 判题信息（json 对象）
     */
    @TableField(value = "judge_result")
    private String judgeResult;

    /**
     * 判题状态（waiting - 待判题、running - 判题中、ac - 通过、fail - 失败）
     */
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}