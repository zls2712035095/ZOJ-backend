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
import com.zack.ZOJ.model.dto.comment.*;
import com.zack.ZOJ.model.entity.Comment;
import com.zack.ZOJ.model.entity.User;
import com.zack.ZOJ.model.vo.UserVO;
import com.zack.ZOJ.service.CommentService;
import com.zack.ZOJ.service.QuestionSubmitService;
import com.zack.ZOJ.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 评论接口
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    /**
     * 创建或回复评论
     *
     * @param commentAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addComment(@RequestBody CommentAddRequest commentAddRequest, HttpServletRequest request) {
        if (commentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentAddRequest, comment);
        comment.setThumbNum(0);
        comment.setFavourNum(0);
        User user = userService.getLoginUser(request);
        comment.setUserId(user.getId());
        comment.setUserName(user.getUserName());
        boolean result = commentService.save(comment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(comment.getId());
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteComment(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Comment oldComment = commentService.getById(id);
        ThrowUtils.throwIf(oldComment == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldComment.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = commentService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param commentUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest) {
        if (commentUpdateRequest == null || commentUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentUpdateRequest, comment);
        long id = commentUpdateRequest.getId();
        // 判断是否存在
        Comment oldComment = commentService.getById(id);
        ThrowUtils.throwIf(oldComment == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = commentService.updateById(comment);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Comments> getCommentById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = commentService.getById(id);
        Comments comments = new Comments();
        BeanUtils.copyProperties(comment, comments);
        List<Comment> chirldren = commentService.getAllByForeignId(comment.getForeignId());
        if (chirldren == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        comments.setChildrenComment(chirldren);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(comments);
    }

    /**
     * 分页获取列表
     *
     * @param commentQueryRequest
     * @return
     */
    @PostMapping("/list/comment/all/page")
    public BaseResponse<Page<Comment>> listCommentByPage(@RequestBody CommentQueryRequest commentQueryRequest) {
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        Page<Comment> commentPage = commentService.page(new Page<>(current, size),
                commentService.getQueryWrapper(commentQueryRequest));
        return ResultUtils.success(commentPage);
    }

    /**
     * 分页获取列表
     *
     * @param commentQueryRequest
     * @return
     */
    @PostMapping("/list/comment/root/page")
    public BaseResponse<Page<Comments>> listCommentsByPage(@RequestBody CommentQueryRequest commentQueryRequest) {
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        List<Comments> comments = commentService.getCommentsPageList(commentQueryRequest.getForeignId());
        Page<Comments> commentsPage = new Page<>(current, size, comments.size());
        commentsPage.setRecords(comments);
        return ResultUtils.success(commentsPage);
    }

    /**
     * 编辑（用户）
     *
     * @param commentEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editComment(@RequestBody CommentEditRequest commentEditRequest, HttpServletRequest request) {
        if (commentEditRequest == null || commentEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentEditRequest, comment);

        User loginUser = userService.getLoginUser(request);
        long id = commentEditRequest.getId();
        // 判断是否存在
        Comment oldComment = commentService.getById(id);
        ThrowUtils.throwIf(oldComment == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldComment.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = commentService.updateById(oldComment);
        return ResultUtils.success(result);
    }


}
