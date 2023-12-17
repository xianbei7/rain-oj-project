package com.rain.oj.model.bo;

import lombok.Data;

import java.util.Date;

/**
 * 题目收藏bo类
 */
@Data
public class QuestionFavourBO {
    /**
     * id
     */
    private Long questionId;

    /**
     * 编号
     */
    private Integer number;

    /**
     * 标题
     */
    private String title;

    /**
     * 难度
     */
    private Integer difficulty;

    /**
     * 标签列表（json 数组）
     */
    private String tags;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建时间
     */
    private Date createTime;
}
