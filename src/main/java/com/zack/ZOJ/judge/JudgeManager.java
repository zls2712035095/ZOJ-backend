package com.zack.ZOJ.judge;

import com.zack.ZOJ.judge.strategy.DefaultJudgeStrategy;
import com.zack.ZOJ.judge.strategy.JavaLanguageJudgeStrategy;
import com.zack.ZOJ.judge.strategy.JudgeContext;
import com.zack.ZOJ.judge.strategy.JudgeStrategy;
import com.zack.ZOJ.model.dto.questionsubmit.JudgeInfo;
import com.zack.ZOJ.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
