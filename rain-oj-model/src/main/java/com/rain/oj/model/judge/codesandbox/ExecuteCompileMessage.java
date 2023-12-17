package com.rain.oj.model.judge.codesandbox;

import lombok.Data;

/**
 * 代码编译消息
 */
@Data
public class ExecuteCompileMessage {

    /**
     * 编译状态
     */
    private Integer exitValue;

    /**
     * 错误信息
     */
    private String message;
}
