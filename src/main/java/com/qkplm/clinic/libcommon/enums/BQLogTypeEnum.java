/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.enums;

import lombok.Getter;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-26 16:26
 */
@Getter
public enum BQLogTypeEnum {
    LOGIN(0, "登录日志"),
    OPERATION(1, "操作日志"),
    SYSTEM(2, "系统日志"),
    LOGOFF(3, "注销日志");

    private final int code;
    private final String name;

    BQLogTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
