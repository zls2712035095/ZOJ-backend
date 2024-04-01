package com.zack.ZOJ.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zack.ZOJ.common.BaseResponse;
import com.zack.ZOJ.common.ErrorCode;
import com.zack.ZOJ.common.PageRequest;
import com.zack.ZOJ.common.ResultUtils;
import com.zack.ZOJ.exception.BusinessException;
import com.zack.ZOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zack.ZOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.zack.ZOJ.model.entity.QuestionSubmit;
import com.zack.ZOJ.model.entity.User;
import com.zack.ZOJ.model.vo.QuestionSubmitVO;
import com.zack.ZOJ.service.QuestionSubmitService;
import com.zack.ZOJ.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交 / 取消提交
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 本次提交变化数
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
            HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userService.getLoginUser(request);
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }


    @PostMapping("list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();

        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size), questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, request));
    }

}
