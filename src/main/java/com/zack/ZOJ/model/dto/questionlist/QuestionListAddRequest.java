package com.zack.ZOJ.model.dto.questionlist;

import com.zack.ZOJ.model.entity.Question;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 */
@Data
public class QuestionListAddRequest implements Serializable {

    /**
     * 题单标题
     */
    private String title;

    /**
     * 题单内容
     */
    private String content;

    /**
     * 题目标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目用例（json 数组）
     */
    private List<QuestionCase> questionCase;


    private static final long serialVersionUID = 1L;
}