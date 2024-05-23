package com.zack.ZOJ.model.dto.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zack.ZOJ.common.PageRequest;
import com.zack.ZOJ.model.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 模块id
     */
    private Long foreignId;

    /**
     * 父级评论id
     */
    private Long pid;

    /**
     * 回复对象
     */
    private String target;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 子评论
     */
    private List<Comment> childrenComment;

    private static final long serialVersionUID = 1L;
}