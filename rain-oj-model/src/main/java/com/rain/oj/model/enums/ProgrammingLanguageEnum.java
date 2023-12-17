package com.rain.oj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 编程语言枚举
 */
public enum ProgrammingLanguageEnum {

    C("c"),
    CPLUSPLUS("cpp"),
    JAVA("java"),
    PYTHON("python"),
    GOLANG("go"),
    JAVASCRIPT("javascript");

    private final String language;


    ProgrammingLanguageEnum(String language) {
        this.language = language;
    }

    /**
     * 获取值列表
     *
     * @return {@link List<String>}
     */
    public static List<String> getLanguages() {
        return Arrays.stream(values()).map(item -> item.language).collect(Collectors.toList());
    }

    /**
     * 根据 language 获取枚举
     *
     * @param language 编程语言
     * @return {@link ProgrammingLanguageEnum}
     */
    public static ProgrammingLanguageEnum getEnumByLanguage(String language) {
        if (ObjectUtils.isEmpty(language)) {
            return null;
        }
        for (ProgrammingLanguageEnum anEnum : ProgrammingLanguageEnum.values()) {
            if (anEnum.language.equals(language)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 判断编程语言是否需要编译
     *
     * @param programmingLanguage 编程语言
     * @return {@link boolean}
     */
    public static boolean needCompile(String programmingLanguage) {
        return programmingLanguage.equals(JAVA.language) || programmingLanguage.equals(C.language) || programmingLanguage.equals(CPLUSPLUS.language);
    }

    public String getLanguage() {
        return language;
    }
}
