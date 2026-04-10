/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.constant.buttons;

import com.qkplm.clinic.libcommon.base.BQButton;

import java.util.Map;

/**
 * @author: Wcke
 * @description: 档案采集器列表按钮
 * @datetime: 2024-07-12 10:31
 */
public class BQFKindListButtons {
    public static final String SEARCH = "FKindList:search";

    /**
     * 映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            SEARCH, new BQButton(SEARCH, "查询", "查询档案采集器列表")
    );
}
