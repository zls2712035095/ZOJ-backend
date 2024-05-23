package com.zack.ZOJ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zack.ZOJ.model.dto.question.QuestionQueryRequest;
import com.zack.ZOJ.model.entity.Userorder;
import com.zack.ZOJ.model.vo.UserorderVo;
import com.zack.ZOJ.service.UserorderService;
import com.zack.ZOJ.mapper.UserorderMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
* @author zls5600
* @description 针对表【userorder(用户排行)】的数据库操作Service实现
* @createDate 2024-05-22 11:56:42
*/
@Service
public class UserorderServiceImpl extends ServiceImpl<UserorderMapper, Userorder>
    implements UserorderService{

    @Override
    public QueryWrapper<UserorderVo> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        return null;
    }

    @Override
    public UserorderVo getUserorderVo(Userorder userorder, HttpServletRequest request) {
        return null;
    }

    @Override
    public Page<UserorderVo> getUserorderVoPage(Page<Userorder> userorderPage, HttpServletRequest request) {
        return null;
    }
}




