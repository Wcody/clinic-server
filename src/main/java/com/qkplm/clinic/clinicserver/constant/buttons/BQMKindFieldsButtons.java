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
 * @description: 病案数据提取字段按钮
 * @datetime: 2024-07-12 09:56
 */
public class BQMKindFieldsButtons {
    public static final String ADD = "MKindFields:save";
    public static final String EDIT = "MKindFields:update";
    public static final String DELETE = "MKindFields:delete";
    public static final String SEARCH = "MKindFields:search";

    /**
     * 映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            ADD, new BQButton(ADD, "新增", "新增数据提取字段信息"),
            EDIT, new BQButton(EDIT, "编辑", "编辑数据提取字段信息"),
            DELETE, new BQButton(DELETE, "删除", "删除数据提取字段信息"),
            SEARCH, new BQButton(SEARCH, "查询", "查询数据提取字段信息列表")
    );
}
