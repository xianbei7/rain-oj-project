package com.rain.oj.model.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Docker镜像枚举
 */
public enum DockerImageEnum {
    C("c", "gcc:9"),
    CPP("cpp", "gcc:9"),
    JAVA("java", "openjdk:8-alpine"),
    PYTHON("python", "python:3.6.9-alpine"),
    CSHARP("csharp", "microsoft/dotnet:2.2-sdk"),
    GO("go", "golang:1.11-alpine"),
    PHP("php", "php:7.2-alpine");
    private final String language;
    private final String image;

    DockerImageEnum(String language, String image) {
        this.language = language;
        this.image = image;
    }

    /**
     * 根据编程语言获取枚举
     *
     * @param language 编程语言
     * @return {@link DockerImageEnum}
     */
    public static DockerImageEnum getEnumByLanguage(String language) {
        if (StringUtils.isBlank(language)) {
            return null;
        }
        for (DockerImageEnum anEnum : DockerImageEnum.values()) {
            if (anEnum.language.equals(language)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getLanguage() {
        return language;
    }

    public String getImage() {
        return image;
    }
}
