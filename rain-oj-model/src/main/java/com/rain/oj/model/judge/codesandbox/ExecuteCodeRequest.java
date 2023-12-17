package com.rain.oj.model.judge.codesandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行代码请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeRequest {

    /**
     * 执行代码沙箱的唯一标识
     */
    private String executeId;
    /**
     * 输入用例
     */
    private List<String> inputList;

    /**
     * 代码
     */
    private String code;

    /**
     * 编程语言
     */
    private String language;
}
