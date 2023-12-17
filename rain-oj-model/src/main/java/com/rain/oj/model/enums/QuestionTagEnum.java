package com.rain.oj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目标签枚举
 */
public enum QuestionTagEnum {

    DESIGN("设计"),
    ARRAY("数组"),
    STRING("字符串"),
    SORT("排序"),
    RECURSION("递归"),
    DFS("深度优先搜索"),
    BFS("广度优先搜索"),
    DYNAMIC_PROGRAMMING("动态规划"),
    GREEDY("贪心"),
    BACKTRACKING("回溯"),
    BINARY_SEARCH("二分查找"),
    HASH("哈希表"),
    LINKED_LIST("链表"),
    BINARY_TREE("二叉树"),
    STACK("栈"),
    QUEUE("队列"),
    GRAPH("图");
    private final String tag;

    QuestionTagEnum(String tag) {
        this.tag = tag;
    }

    public static List<String> getTags() {
        return Arrays.stream(values()).map(item -> item.tag).collect(Collectors.toList());
    }

    /**
     * 根据 tag 获取枚举
     *
     * @param tag 标签
     * @return {@link QuestionTagEnum}
     */
    public static QuestionTagEnum getEnumByText(String tag) {
        if (ObjectUtils.isEmpty(tag)) {
            return null;
        }
        for (QuestionTagEnum anEnum : QuestionTagEnum.values()) {
            if (anEnum.tag.equals(tag)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getTag() {
        return tag;
    }
}
