package com.zack.ZOJ.judge.codesandbox.impl;

import com.zack.ZOJ.judge.codesandbox.CodeSandbox;
import com.zack.ZOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.zack.ZOJ.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用现成接口的沙箱）
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("ThirdPartyCodeSandbox");
        return null;
    }
}
