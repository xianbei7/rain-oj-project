package com.rain.oj.model.judge;

import lombok.Data;

/**
 * 判题信息
 */
@Data
public class JudgeResult {

    /**
     * 程序执行类型
     */
    private String type;

    /**
     * 程序执行错误详细信息（哪里报错了）
     */
    private String message;

    /**
     * 程序执行正确率
     */
    private Integer correctRate;
}
