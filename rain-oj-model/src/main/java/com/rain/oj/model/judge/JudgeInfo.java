package com.rain.oj.model.judge;

import lombok.Data;

/**
 * 测试用例判题信息
 */
@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private Boolean success;

    /**
     * 消耗时间
     */
    private Long time;

    /**
     * 消耗内存
     */
    private Long memory;
}
