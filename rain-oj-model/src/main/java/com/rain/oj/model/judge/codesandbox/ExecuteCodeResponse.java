package com.rain.oj.model.judge.codesandbox;

import com.rain.oj.model.judge.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行代码响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {

    /**
     * 执行代码沙箱的唯一标识
     */
    private String executeId;

    /**
     * 执行是否全部成功
     */
    private Boolean isAllSuccess;

    /**
     * 错误类型
     */
    private String errorType;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 判题相关信息（运行时间、内存）
     */
    private List<JudgeInfo> judgeInfoList;

    /**
     * 输出用例
     */
    private List<String> outputList;
}
