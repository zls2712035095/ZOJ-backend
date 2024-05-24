package com.zack.ZOJ.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zack.ZOJ.annotation.AuthCheck;
import com.zack.ZOJ.common.BaseResponse;
import com.zack.ZOJ.common.DeleteRequest;
import com.zack.ZOJ.common.ErrorCode;
import com.zack.ZOJ.common.ResultUtils;
import com.zack.ZOJ.constant.UserConstant;
import com.zack.ZOJ.exception.BusinessException;
import com.zack.ZOJ.exception.ThrowUtils;
import com.zack.ZOJ.model.dto.question.*;
import com.zack.ZOJ.model.dto.questionlist.*;
import com.zack.ZOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zack.ZOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.zack.ZOJ.model.dto.userrank.UserRankQueryRequest;
import com.zack.ZOJ.model.entity.*;
import com.zack.ZOJ.model.vo.QuestionListVO;
import com.zack.ZOJ.model.vo.QuestionSubmitVO;
import com.zack.ZOJ.model.vo.QuestionVO;
import com.zack.ZOJ.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PipedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目接口
 */
@RestController
@RequestMapping("/questionList")
@Slf4j
public class QuestionListController {

    @Resource
    private QuestionListService questionListService;

    @Resource
    private UserService userService;

    @Resource
    private QuestionService questionService;

    @Resource
    private UserRankService userRankService;


    /**
     * 创建
     *
     * @param questionListAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestionList(@RequestBody QuestionListAddRequest questionListAddRequest, HttpServletRequest request) {
        if (questionListAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionList questionList = new QuestionList();
        BeanUtils.copyProperties(questionListAddRequest, questionList);
        List<String> tags = questionListAddRequest.getTags();
        if (tags != null) {
            questionList.setTags(JSONUtil.toJsonStr(tags));
        }
        List<QuestionCase> questionsId = questionListAddRequest.getQuestionCase();
        List<Question> questionCases = questionsId.stream().map(questionId -> questionService.getById(questionId.getInput())).collect(Collectors.toList());
        if (questionCases != null) {
            questionList.setQuestionCase(JSONUtil.toJsonStr(questionCases));
        }
        questionListService.validQuestionList(questionList, true);
        User loginUser = userService.getLoginUser(request);
        questionList.setUserId(loginUser.getId());
        questionList.setFavourNum(0);
        questionList.setThumbNum(0);
        boolean result = questionListService.save(questionList);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionListId = questionList.getId();
        return ResultUtils.success(newQuestionListId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestionList(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        QuestionList oldQuestionList = questionListService.getById(id);
        ThrowUtils.throwIf(oldQuestionList == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestionList.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = questionListService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionListUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestionList(@RequestBody QuestionListUpdateRequest questionListUpdateRequest) {
        if (questionListUpdateRequest == null || questionListUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionList questionList = new QuestionList();
        BeanUtils.copyProperties(questionListUpdateRequest, questionList);
        List<String> tags = questionListUpdateRequest.getTags();
        if (tags != null) {
            questionList.setTags(JSONUtil.toJsonStr(tags));
        }
        List<QuestionCase> questionsId = questionListUpdateRequest.getQuestionCase();
        List<Question> questionCases = questionsId.stream().map(questionId -> questionService.getById(questionId.getInput())).collect(Collectors.toList());
        if (questionCases != null) {
            questionList.setQuestionCase(JSONUtil.toJsonStr(questionCases));
        }
        // 参数校验
        questionListService.validQuestionList(questionList, false);
        long id = questionListUpdateRequest.getId();
        // 判断是否存在
        QuestionList oldQuestionList = questionListService.getById(id);
        ThrowUtils.throwIf(oldQuestionList == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = questionListService.updateById(questionList);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取（脱敏）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionListVO> getQuestionListVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionList questionList = questionListService.getById(id);
        if (questionList == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionListService.getQuestionListVO(questionList, request));
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<QuestionListUpdateRequest> getQuestionListById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionList questionList = questionListService.getById(id);
        if (questionList == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        //不是本人或管理员, 不能获取全部数据
        if(!questionList.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        QuestionListVO questionListVO = questionListService.getQuestionListVO(questionList, request);
        QuestionListUpdateRequest questionListUpdateRequest = new QuestionListUpdateRequest();
        BeanUtils.copyProperties(questionListVO, questionListUpdateRequest);
        List<QuestionCase> list = questionListVO.getQuestionCase().stream().map(question -> {
            QuestionCase questionCase = new QuestionCase();
            questionCase.setInput(question.getId());
            return questionCase;
        }).collect(Collectors.toList());
        questionListUpdateRequest.setQuestionCase(list);
        return ResultUtils.success(questionListUpdateRequest);
    }
    /**
     * 分页获取列表（仅管理员）
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<QuestionList>> listQuestionListByPage(@RequestBody QuestionListQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        Page<QuestionList> questionListPage = questionListService.page(new Page<>(current, size),
                questionListService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionListPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionListQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionListVO>> listQuestionListVOByPage(@RequestBody QuestionListQueryRequest questionListQueryRequest,
            HttpServletRequest request) {
        long current = questionListQueryRequest.getCurrent();
        long size = questionListQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<QuestionList> questionListPage = questionListService.page(new Page<>(current, size),
                questionListService.getQueryWrapper(questionListQueryRequest));
        return ResultUtils.success(questionListService.getQuestionListVOPage(questionListPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionListQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionListVO>> listMyQuestionListVOByPage(@RequestBody QuestionListQueryRequest questionListQueryRequest,
            HttpServletRequest request) {
        if (questionListQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        questionListQueryRequest.setUserId(loginUser.getId());
        long current = questionListQueryRequest.getCurrent();
        long size = questionListQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<QuestionList> questionListPage = questionListService.page(new Page<>(current, size),
                questionListService.getQueryWrapper(questionListQueryRequest));
        return ResultUtils.success(questionListService.getQuestionListVOPage(questionListPage, request));
    }

    // endregion

//    /**
//     * 分页搜索（从 ES 查询，封装类）
//     *
//     * @param questionQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/search/page/vo")
//    public BaseResponse<Page<QuestionVO>> searchQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
//            HttpServletRequest request) {
//        long size = questionQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<Question> questionPage = questionService.searchFromEs(questionQueryRequest);
//        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
//    }

    /**
     * 编辑（用户）
     *
     * @param questionListEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestionList(@RequestBody QuestionListEditRequest questionListEditRequest, HttpServletRequest request) {
        if (questionListEditRequest == null || questionListEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionList questionList = new QuestionList();
        BeanUtils.copyProperties(questionListEditRequest, questionList);
        List<String> tags = questionListEditRequest.getTags();
        if (tags != null) {
            questionList.setTags(JSONUtil.toJsonStr(tags));
        }
        List<QuestionCase> questionsId = questionListEditRequest.getQuestionCase();
        List<Question> questionCases = questionsId.stream().map(questionId -> questionService.getById(questionId.getInput())).collect(Collectors.toList());
        if (questionCases != null) {
            questionList.setQuestionCase(JSONUtil.toJsonStr(questionCases));
        }
        // 参数校验
        questionListService.validQuestionList(questionList, false);
        User loginUser = userService.getLoginUser(request);
        long id = questionListEditRequest.getId();
        // 判断是否存在
        QuestionList oldQuestionList = questionListService.getById(id);
        ThrowUtils.throwIf(oldQuestionList == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestionList.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = questionListService.updateById(questionList);
        return ResultUtils.success(result);
    }

}
