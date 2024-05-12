package com.zack.ZOJ.judge;

import com.zack.ZOJ.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
