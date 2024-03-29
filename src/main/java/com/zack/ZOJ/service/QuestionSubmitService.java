package com.zack.ZOJ.service;

import com.zack.ZOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zack.ZOJ.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zack.ZOJ.model.entity.User;

/**
* @author admin
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-03-29 10:21:55
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

//    /**
//     * 题目提交（内部服务）
//     *
//     * @param userId
//     * @param questionId
//     * @return
//     */
//    int doQuestionSubmitInner(long userId, long questionId);
}
