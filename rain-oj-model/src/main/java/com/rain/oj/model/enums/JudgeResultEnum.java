package com.rain.oj.model.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 判题信息消息枚举
 */
public enum JudgeResultEnum {

    ACCEPTED("回答正确"),
    WRONG_ANSWER("解答错误"),
    COMPILE_ERROR("编译错误"),
    RUNTIME_ERROR("运行错误"),
    MEMORY_LIMIT_EXCEEDED("内存溢出"),
    TIME_LIMIT_EXCEEDED("超时"),
    OUTPUT_LIMIT_EXCEEDED("输出溢出"),
    WAITING("等待中"),
    DANGEROUS_OPERATION("危险操作"),
    SYSTEM_ERROR("系统错误"),
    JUDGE_FAIL("判题失败");

    private final String type;

    JudgeResultEnum(String type) {
        this.type = type;
    }

    public static boolean needJudge(String errorType) {
        return !(StringUtils.isNotBlank(errorType) && (COMPILE_ERROR.type.equals(errorType) || RUNTIME_ERROR.type.equals(errorType)));
    }

    public String getType() {
        return type;
    }
}
