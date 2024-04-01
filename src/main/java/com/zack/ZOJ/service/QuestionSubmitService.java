package com.zack.ZOJ.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zack.ZOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zack.ZOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.zack.ZOJ.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zack.ZOJ.model.entity.User;
import com.zack.ZOJ.model.vo.QuestionSubmitVO;
import javax.servlet.http.HttpServletRequest;

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

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param request
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, HttpServletRequest request);
//    /**
//     * 题目提交（内部服务）
//     *
//     * @param userId
//     * @param questionId
//     * @return
//     */
//    int doQuestionSubmitInner(long userId, long questionId);
}
