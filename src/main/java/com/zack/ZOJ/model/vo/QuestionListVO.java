package com.zack.ZOJ.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.zack.ZOJ.model.dto.question.JudgeConfig;
import com.zack.ZOJ.model.dto.questionlist.QuestionCase;
import com.zack.ZOJ.model.entity.Question;
import com.zack.ZOJ.model.entity.QuestionList;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题单
 * @TableName question_list
 */
@Data
public class QuestionListVO implements Serializable {
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
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 持续时间
     */
    private Date activeTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 创建题目人的信息
     */
    private UserVO userVO;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param questionListVO
     * @return
     */
    public static QuestionList voToObj(QuestionListVO questionListVO) {
        if (questionListVO == null) {
            return null;
        }
        QuestionList questionList = new QuestionList();
        BeanUtils.copyProperties(questionListVO, questionList);
        List<String> tagList = questionListVO.getTags();
        if (tagList != null) {
            questionList.setTags(JSONUtil.toJsonStr(tagList));
        }
        List<Question> questionCase = questionListVO.getQuestionCase();
        if (questionCase != null) {
            questionList.setQuestionCase(JSONUtil.toJsonStr(questionCase));
        }
        return questionList;
    }

    /**
     * 对象转包装类
     *
     * @param questionList
     * @return
     */
    public static QuestionListVO objToVo(QuestionList questionList) {
        if (questionList == null) {
            return null;
        }
        QuestionListVO questionListVO = new QuestionListVO();
        BeanUtils.copyProperties(questionList, questionListVO);
        List<String> tagList = JSONUtil.toList(questionList.getTags(), String.class);
        questionListVO.setTags(tagList);
        List<Question> questionCase = JSONUtil.toList(questionList.getQuestionCase(), Question.class);
        if (questionCase != null) {
            questionListVO.setQuestionCase(questionCase);
        }
        return questionListVO;
    }
}