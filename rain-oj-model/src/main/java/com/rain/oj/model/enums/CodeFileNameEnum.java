package com.rain.oj.model.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 代码文件名枚举
 */
public enum CodeFileNameEnum {
    C("c", "main.c"),
    CPLUSPLUS("cpp", "main.cpp"),
    JAVA("java", "Main.java"),
    PYTHON("python", "main.py");
    private final String language;
    private final String name;

    CodeFileNameEnum(String language, String name) {
        this.language = language;
        this.name = name;
    }

    /**
     * 根据编程语言获取枚举
     *
     * @param language 编程语言
     * @return {@link CodeFileNameEnum}
     */
    public static CodeFileNameEnum getEnumByLanguage(String language) {
        if (StringUtils.isBlank(language)) {
            return null;
        }
        for (CodeFileNameEnum anEnum : CodeFileNameEnum.values()) {
            if (anEnum.language.equals(language)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
