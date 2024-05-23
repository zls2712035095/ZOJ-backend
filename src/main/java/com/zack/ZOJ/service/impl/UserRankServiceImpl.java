package com.zack.ZOJ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zack.ZOJ.common.ErrorCode;
import com.zack.ZOJ.constant.CommonConstant;
import com.zack.ZOJ.exception.BusinessException;
import com.zack.ZOJ.model.dto.userrank.UserRankQueryRequest;
import com.zack.ZOJ.model.entity.User;
import com.zack.ZOJ.model.entity.UserRank;
import com.zack.ZOJ.service.UserRankService;
import com.zack.ZOJ.mapper.UserRankMapper;
import com.zack.ZOJ.service.UserService;
import com.zack.ZOJ.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.zack.ZOJ.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author zls5600
 * @description 针对表【user_rank(用户排行)】的数据库操作Service实现
 * @createDate 2024-05-22 12:40:19
 */
@Service
public class UserRankServiceImpl extends ServiceImpl<UserRankMapper, UserRank>
        implements UserRankService {

    @Resource
    private UserService userService;
    @Override
    public long userRankAdd(long userId) {
        // 1. 校验
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        // 记录不能重复
        QueryWrapper<UserRank> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "记录重复");
        }
        User user = userService.getById(userId);
        String userName = user.getUserName();
        String userAvatar = user.getUserAvatar();
        String userProfile = user.getUserProfile();
        // 插入数据
        UserRank userRank = new UserRank();
        userRank.setUserId(userId);
        userRank.setUserName(userName);
        userRank.setUserAvatar(userAvatar);
        userRank.setUserProfile(userProfile);
        userRank.setAcNum(0);
        userRank.setSubmitNum(0);

        boolean saveResult = this.save(userRank);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存记录失败，数据库错误");
        }
        return userRank.getId();
    }

    @Override
    public long getUserRankIdByUserId(long userId) {
        // 1. 校验
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        QueryWrapper<UserRank> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        UserRank userRank = this.baseMapper.selectOne(queryWrapper);
        if (userRank == null) {
            userRank = getById(userRankAdd(userId));
        }
        return userRank.getId();
    }

    @Override
    public int getUserRankSubmitNum(long userId) {
        long userRankId = getUserRankIdByUserId(userId);
        UserRank userRank = getById(userRankId);
        return userRank.getSubmitNum();
    }

    @Override
    public int getUserRankAcNum(long userId) {
        long userRankId = getUserRankIdByUserId(userId);
        UserRank userRank = getById(userRankId);
        return userRank.getAcNum();
    }

    @Override
    public UserRank getUserRank(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = userService.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        QueryWrapper<UserRank> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        UserRank userRank = this.baseMapper.selectOne(queryWrapper);
        if (userRank == null) {
            userRank = getById(userRankAdd(userId));
        }
        return userRank;
    }

    @Override
    public QueryWrapper<UserRank> getQueryWrapper(UserRankQueryRequest userRankQueryRequest) {
        if (userRankQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userRankQueryRequest.getId();
        Long userId = userRankQueryRequest.getUserId();
        String userName = userRankQueryRequest.getUserName();
        String userProfile = userRankQueryRequest.getUserProfile();
        Integer acNum = userRankQueryRequest.getAcNum();
        Integer submitNum = userRankQueryRequest.getSubmitNum();
        String sortField = userRankQueryRequest.getSortField();
        String sortOrder = userRankQueryRequest.getSortOrder();
        QueryWrapper<UserRank> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(userId != null, "userId", userId);
        queryWrapper.eq(acNum != null, "acNum", acNum);
        queryWrapper.eq(submitNum != null, "submitNum", submitNum);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




