package com.zack.ZOJ.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.zack.ZOJ.model.dto.question.JudgeCase;
import com.zack.ZOJ.model.dto.question.JudgeConfig;
import com.zack.ZOJ.model.dto.questionsubmit.JudgeInfo;
import com.zack.ZOJ.model.entity.Question;
import com.zack.ZOJ.model.enums.JudgeResultEnum;

import java.util.List;

/**
 * 默认判题策略
 */
public class JavaLanguageJudgeStrategy implements JudgeStrategy{

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        JudgeResultEnum judgeResultEnum = JudgeResultEnum.Accepted;
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);
        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (outputList.size() != inputList.size()) {
            judgeResultEnum = JudgeResultEnum.WrongAnswer;
            judgeInfoResponse.setMessage(judgeResultEnum.getValue());
            return judgeInfoResponse;
        }
        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeResultEnum = JudgeResultEnum.WrongAnswer;
                judgeInfoResponse.setMessage(judgeResultEnum.getValue());
                return judgeInfoResponse;
            }
        }
        // 判断题目限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();
        if (memory > needMemoryLimit) {
            judgeResultEnum = JudgeResultEnum.MemoryLimitExceeded;
            judgeInfoResponse.setMessage(judgeResultEnum.getValue());
            return judgeInfoResponse;
        }
        if (time > needTimeLimit) {
            judgeResultEnum = JudgeResultEnum.TimeLimitExceeded;
            judgeInfoResponse.setMessage(judgeResultEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeResultEnum.getValue());
        return judgeInfoResponse;
    }
}
