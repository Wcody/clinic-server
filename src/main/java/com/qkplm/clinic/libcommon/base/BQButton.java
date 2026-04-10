/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Wcke
 * @description: 菜单下的按钮信息原型
 * @datetime: 2024-06-27 21:44
 */
@Getter
@AllArgsConstructor
public class BQButton {
    private String name;
    private String title;
    private String description;
}
