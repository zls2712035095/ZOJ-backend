package com.zack.ZOJ.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zack.ZOJ.model.entity.Question;
import com.zack.ZOJ.service.QuestionService;
import com.zack.ZOJ.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-03-29 10:20:48
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




