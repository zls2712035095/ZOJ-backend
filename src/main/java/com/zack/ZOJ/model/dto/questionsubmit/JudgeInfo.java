package com.zack.ZOJ.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目信息
 */
@Data
public class JudgeInfo implements Serializable {

    /**
     * 程序执行信息
     */
    private Long message;

    /**
     * 消耗内存（KB）
     */
    private Long memory;

    /**
     * 消耗时间（KB）
     */
    private Long time;
}