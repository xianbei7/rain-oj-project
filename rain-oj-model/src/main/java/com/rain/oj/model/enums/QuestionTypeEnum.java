package com.rain.oj.model.enums;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目类型枚举
 */
public enum QuestionTypeEnum {

    ACM("ACM模式", 1),
    CORE("核心代码模式", 2),
    SQL("SQL", 3);

    private final String type;

    private final Integer value;

    QuestionTypeEnum(String text, Integer value) {
        this.type = text;
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
     * 根据 type 获取枚举
     *
     * @param type 类型
     * @return {@link QuestionTypeEnum}
     */
    public static QuestionTypeEnum getEnumByType(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        for (QuestionTypeEnum anEnum : QuestionTypeEnum.values()) {
            if (anEnum.type.equals(type)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 值
     * @return {@link QuestionTypeEnum}
     */
    public static QuestionTypeEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (QuestionTypeEnum anEnum : QuestionTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
