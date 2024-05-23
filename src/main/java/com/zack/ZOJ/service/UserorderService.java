package com.zack.ZOJ.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zack.ZOJ.model.dto.question.QuestionQueryRequest;
import com.zack.ZOJ.model.entity.Question;
import com.zack.ZOJ.model.entity.Userorder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zack.ZOJ.model.vo.QuestionVO;
import com.zack.ZOJ.model.vo.UserorderVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author zls5600
* @description 针对表【userorder(用户排行)】的数据库操作Service
* @createDate 2024-05-22 11:56:42
*/
public interface UserorderService extends IService<Userorder> {


    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<UserorderVo> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param userorder
     * @param request
     * @return
     */
    UserorderVo getUserorderVo(Userorder userorder, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param userorderPage
     * @param request
     * @return
     */
    Page<UserorderVo> getUserorderVoPage(Page<Userorder> userorderPage, HttpServletRequest request);
}
