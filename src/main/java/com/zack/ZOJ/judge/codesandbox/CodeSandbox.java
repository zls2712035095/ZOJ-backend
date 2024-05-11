package com.zack.ZOJ.judge.codesandbox;

import com.zack.ZOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.zack.ZOJ.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
