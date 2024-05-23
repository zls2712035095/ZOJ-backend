package com.zack.ZOJ.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zack.ZOJ.model.dto.user.UserQueryRequest;
import com.zack.ZOJ.model.dto.userrank.UserRankQueryRequest;
import com.zack.ZOJ.model.entity.User;
import com.zack.ZOJ.model.entity.UserRank;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zack.ZOJ.model.vo.LoginUserVO;
import com.zack.ZOJ.model.vo.UserVO;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author zls5600
* @description 针对表【user_rank(用户排行)】的数据库操作Service
* @createDate 2024-05-22 12:40:19
*/
public interface UserRankService extends IService<UserRank> {
    /**
     * 增加用户记录
     *
     * @param userId   用户id
     * @return 新用户记录 id
     */
    long userRankAdd(long userId);

    /**
     * 通过userid得到排行的id
     *
     * @param userId   用户id
     * @return 用户记录 id
     */
    long getUserRankIdByUserId(long userId);

    /**
     * 通过userid得到提交总数
     *
     * @param userId   用户id
     * @return 用户记录 id
     */
    int getUserRankSubmitNum(long userId);

    /**
     * 通过userid得到Ac总数
     *
     * @param userId   用户id
     * @return 用户记录 id
     */
    int getUserRankAcNum(long userId);

    /**
     * 获取当前用户记录
     *
     * @param request
     * @return
     */
    UserRank getUserRank(HttpServletRequest request);


    /**
     * 获取查询条件
     *
     * @param userRankQueryRequest
     * @return
     */
    QueryWrapper<UserRank> getQueryWrapper(UserRankQueryRequest userRankQueryRequest);
}
