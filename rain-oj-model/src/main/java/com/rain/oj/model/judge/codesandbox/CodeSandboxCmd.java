package com.rain.oj.model.judge.codesandbox;

import lombok.Data;

/**
 * 代码沙箱命令
 */
@Data
public class CodeSandboxCmd {
    private String compileCmd;
    private String runCmd;
}