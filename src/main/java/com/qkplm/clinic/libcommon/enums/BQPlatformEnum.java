/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.enums;

import lombok.Getter;

/**
 * @author: Wcke
 * @description: 登录系统的平台枚举
 * @datetime: 2024-06-16 16:12
 */
@Getter
public enum BQPlatformEnum {
    //pb表示电脑端浏览器，mb表示手机浏览器，ma表示移动APP,pc表示电脑应用程序
    PCC("PCC", "PC客户端"),
    MBC("MBC", "移动APP端"),
    MBB("MBB", "移动浏览器端"),
    PCB("PCB", "PC浏览器端");

    private final String value;
    private final String name;

    BQPlatformEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }
}
