package com.rain.oj.model.judge.codesandbox;

import lombok.Data;

/**
 * 代码运行信息
 */
@Data
public class ExecuteRunMessage {

    /**
     * 运行信息
     */
    private String message;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 运行时间
     */
    private Long time;
}
