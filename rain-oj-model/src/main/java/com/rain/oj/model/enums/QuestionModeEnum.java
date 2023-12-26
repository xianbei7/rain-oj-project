package com.rain.oj.model.enums;


import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 题目类型枚举
 */
@Getter
public enum QuestionModeEnum {

    ACM("ACM模式", 1),
    CORE("核心代码模式", 2),
    SQL("SQL", 3);

    private final String mode;

    private final Integer value;

    QuestionModeEnum(String mode, Integer value) {
        this.mode = mode;
        this.value = value;
    }

    /**
     * 根据 mode 获取枚举
     *
     * @param mode 类型
     * @return {@link QuestionModeEnum}
     */
    public static QuestionModeEnum getEnumByMode(String mode) {
        if (StringUtils.isBlank(mode)) {
            return null;
        }
        for (QuestionModeEnum anEnum : QuestionModeEnum.values()) {
            if (anEnum.mode.equals(mode)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 值
     * @return {@link QuestionModeEnum}
     */
    public static QuestionModeEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (QuestionModeEnum anEnum : QuestionModeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
