package com.zack.ZOJ.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zack.ZOJ.model.dto.comment.CommentQueryRequest;
import com.zack.ZOJ.model.dto.comment.Comments;
import com.zack.ZOJ.model.dto.question.QuestionQueryRequest;
import com.zack.ZOJ.model.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zack.ZOJ.model.entity.Question;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zls5600
* @description 针对表【comment(评论)】的数据库操作Service
* @createDate 2024-05-22 17:03:24
*/
@Service
public interface CommentService extends IService<Comment> {
    /**
     * 得到当前模块的所有评论
     * @param forigenId
     * @return
     */
    List<Comment> getAllByForeignId(Long forigenId);

    /**
     * 得到当前模块的顶级评论
     * @param forigenId
     * @return
     */
    List<Comments> getCommentsPageList(Long forigenId);

    QueryWrapper<Comment>  getQueryWrapper(CommentQueryRequest commentQueryRequest);

}
