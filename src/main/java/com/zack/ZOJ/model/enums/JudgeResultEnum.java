package com.zack.ZOJ.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件上传业务类型枚举
 */
public enum JudgeResultEnum {

    Accepted("成功", "Accepted"),
    WrongAnswer("WrongAnswer", "WrongAnswer"),
    CompileError("CompileError", "CompileError"),
    MemoryLimitExceeded("MemoryLimitExceeded", "MemoryLimitExceeded"),
    TimeLimitExceeded("TimeLimitExceeded", "TimeLimitExceeded"),
    PresentationError("PresentationError", "PresentationError"),
    Waiting("Waiting", "Waiting"),
    OutputLimitExceeded("OutputLimitExceeded", "OutputLimitExceeded"),
    DangerousOperation("DangerousOperation", "DangerousOperation"),
    RuntimeError("RuntimeError", "RuntimeError"),
    SystemError("SystemError", "SystemError");

    private final String text;

    private final String value;

    JudgeResultEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeResultEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeResultEnum anEnum : JudgeResultEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
