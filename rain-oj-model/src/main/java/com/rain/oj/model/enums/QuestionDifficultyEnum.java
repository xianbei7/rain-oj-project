package com.rain.oj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

/**
 * 题目难度枚举
 */
public enum QuestionDifficultyEnum {

    EASY("简单", 1),
    MIDDLE("中等", 2),
    HARD("困难", 3);

    private final String difficulty;

    private final Integer value;

    QuestionDifficultyEnum(String difficulty, Integer value) {
        this.difficulty = difficulty;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 值
     * @return {@link QuestionDifficultyEnum}
     */
    public static QuestionDifficultyEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionDifficultyEnum anEnum : QuestionDifficultyEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据 difficulty 获取枚举
     *
     * @param difficulty 难度
     * @return {@link QuestionDifficultyEnum}
     */
    public static QuestionDifficultyEnum getEnumByText(String difficulty) {
        if (ObjectUtils.isEmpty(difficulty)) {
            return null;
        }
        for (QuestionDifficultyEnum anEnum : QuestionDifficultyEnum.values()) {
            if (anEnum.difficulty.equals(difficulty)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
