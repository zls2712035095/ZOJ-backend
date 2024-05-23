package com.zack.ZOJ.model.dto.questionlist;

import com.zack.ZOJ.common.PageRequest;
import com.zack.ZOJ.model.entity.Question;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionListQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题单标题
     */
    private String title;

    /**
     * 题单内容
     */
    private String content;

    /**
     * 题单标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目用例（json 数组）
     */
    private List<Question> questionCase;

    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}