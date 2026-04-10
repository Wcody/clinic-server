/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.enums;

import lombok.Getter;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-20 23:02
 */
@Getter
public enum BQMenuTypeEnum {
    MENU(0, "菜单"),
    IFRAME(1, "iframe"),
    LINK(2, "外链"),
    BUTTON(3, "按钮"),
    ;

    private final Integer code;
    private final String desc;

    BQMenuTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
