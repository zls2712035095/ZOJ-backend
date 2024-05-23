package com.zack.ZOJ.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zack.ZOJ.model.dto.question.QuestionQueryRequest;
import com.zack.ZOJ.model.dto.questionlist.QuestionListQueryRequest;
import com.zack.ZOJ.model.entity.Question;
import com.zack.ZOJ.model.entity.QuestionList;
import com.zack.ZOJ.model.vo.QuestionListVO;
import com.zack.ZOJ.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author zls5600
* @description 针对表【question_list(题单)】的数据库操作Service
* @createDate 2024-05-23 13:03:52
*/
public interface QuestionListService extends IService<QuestionList> {
    /**
     * 校验
     *
     * @param questionList
     * @param add
     */
    void validQuestionList(QuestionList questionList, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionListQueryRequest
     * @return
     */
    QueryWrapper<QuestionList> getQueryWrapper(QuestionListQueryRequest questionListQueryRequest);


    /**
     * 获取题目封装
     *
     * @param questionList
     * @param request
     * @return
     */
    QuestionListVO getQuestionListVO(QuestionList questionList, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionListPage
     * @param request
     * @return
     */
    Page<QuestionListVO> getQuestionListVOPage(Page<QuestionList> questionListPage, HttpServletRequest request);
}
