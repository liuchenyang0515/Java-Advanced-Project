package com.me.enums;

/**
 * @Desc: 是否 枚举
 */
public enum YesOrNo {
    NO(0, "否"),
    YES(1, "是");

    private final Integer code;
    private final String value;

    YesOrNo(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
