package com.rain.oj.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目实体类
 */
@TableName(value = "question")
@Data
public class Question implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 编号
     */
    private Integer number;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private String tags;

    /**
     * 难度
     */
    private Integer difficulty;

    /**
     * 题目模式（ACM、核心代码、SQL）
     */
    private Integer mode;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 题目提交数
     */
    @TableField(value = "submit_num")
    private Integer submitNum;

    /**
     * 题目通过数
     */
    @TableField(value = "accepted_num")
    private Integer acceptedNum;

    /**
     * 判题用例（json 数组）
     */
    @TableField(value = "judge_case")
    private String judgeCase;

    /**
     * 判题配置（json 数组）
     */
    @TableField(value = "judge_config")
    private String judgeConfig;

    /**
     * 点赞数
     */
    @TableField(value = "thumb_num")
    private Integer thumbNum;

    /**
     * 收藏数
     */
    @TableField(value = "favour_num")
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    @TableField(value = "user_id")
    private Long userId;

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

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}