package com.rain.oj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目提交状态枚举
 */
public enum QuestionSubmitStatusEnum {

    WAITING("等待判题中", 0),
    RUNNING("判题中", 1),
    ACCEPTED("通过", 2),
    FAIL("失败", 3);

    private final String status;

    private final Integer value;

    QuestionSubmitStatusEnum(String status, Integer value) {
        this.status = status;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return {@link List<Integer>}
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 status 获取枚举
     *
     * @param status 值
     * @return {@link QuestionSubmitStatusEnum}
     */
    public static QuestionSubmitStatusEnum getEnumByStatus(String status) {
        if (ObjectUtils.isEmpty(status)) {
            return null;
        }
        for (QuestionSubmitStatusEnum anEnum : QuestionSubmitStatusEnum.values()) {
            if (anEnum.status.equals(status)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 值
     * @return {@link QuestionSubmitStatusEnum}
     */
    public static QuestionSubmitStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitStatusEnum anEnum : QuestionSubmitStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getStatus() {
        return status;
    }
}
