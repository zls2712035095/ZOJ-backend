package com.zack.ZOJ.model.dto.questionlist;

import com.zack.ZOJ.model.entity.Question;
import lombok.Data;

import java.io.Serializable;

/**
 * 题目用例
 */
@Data
public class QuestionCase implements Serializable {

    private Question question;
}
