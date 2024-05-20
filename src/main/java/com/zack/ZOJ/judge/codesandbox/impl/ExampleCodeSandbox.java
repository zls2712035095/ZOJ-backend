package com.zack.ZOJ.judge.codesandbox.impl;

import com.zack.ZOJ.judge.codesandbox.CodeSandbox;
import com.zack.ZOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.zack.ZOJ.judge.codesandbox.model.ExecuteCodeResponse;
import com.zack.ZOJ.judge.codesandbox.model.JudgeInfo;
import com.zack.ZOJ.model.enums.JudgeResultEnum;
import com.zack.ZOJ.model.enums.QuestionSubmitEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 示例代码沙箱（示例）
 */
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeResultEnum.Accepted.getText());
        judgeInfo.setMemory(100l);
        judgeInfo.setTime(100l);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
