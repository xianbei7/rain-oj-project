package com.rain.oj.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目代码模板实体类
 */
@TableName(value = "question_template")
@Data
public class QuestionTemplate implements Serializable {

    /**
     * 题目 id
     */
    @TableId(value = "question_id", type = IdType.INPUT)
    private Long questionId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 代码模板
     */
    @TableField(value = "code_template")
    private String codeTemplate;

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