package com.rain.oj.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 题目提交状态枚举
 */
@Getter
public enum QuestionSubmissionStatusEnum {
    WAITING("等待判题中", "waiting"),
    RUNNING("判题中", "running"),
    ACCEPTED("通过", "ac"),
    FAIL("失败", "fail");

    private final String display;

    private final String status;

    QuestionSubmissionStatusEnum(String display, String status) {
        this.display = display;
        this.status = status;
    }

    /**
     * 根据 statusDisplay 获取枚举
     *
     * @param display 值
     * @return {@link QuestionSubmissionStatusEnum}
     */
    public static QuestionSubmissionStatusEnum getEnumByDisplay(String display) {
        if (ObjectUtils.isEmpty(display)) {
            return null;
        }
        for (QuestionSubmissionStatusEnum anEnum : QuestionSubmissionStatusEnum.values()) {
            if (anEnum.display.equals(display)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据 status 获取枚举
     *
     * @param status 值
     * @return {@link QuestionSubmissionStatusEnum}
     */
    public static QuestionSubmissionStatusEnum getEnumByStatus(String status) {
        if (ObjectUtils.isEmpty(status)) {
            return null;
        }
        for (QuestionSubmissionStatusEnum anEnum : QuestionSubmissionStatusEnum.values()) {
            if (anEnum.status.equals(status)) {
                return anEnum;
            }
        }
        return null;
    }
}
