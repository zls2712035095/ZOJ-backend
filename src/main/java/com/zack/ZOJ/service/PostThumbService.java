package com.zack.ZOJ.service;

import com.zack.ZOJ.model.entity.PostThumb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zack.ZOJ.model.entity.User;

/**
 * 帖子点赞服务
 *
 * @author <a href="https://github.com/lizack">程序员鱼皮</a>
 * @from <a href="https://zack.icu">编程导航知识星球</a>
 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostThumb(long postId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doPostThumbInner(long userId, long postId);
}
