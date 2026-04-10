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
 * @description: 病案分类按钮
 * @datetime: 2024-07-12 09:49
 */
public class BQMKindButtons {
    public static final String ADD = "MKind:save";
    public static final String EDIT = "MKind:update";
    public static final String DELETE = "MKind:delete";
    public static final String SEARCH = "MKind:search";
    public static final String SET_STATUS = "MKind:setStatus";

    /**
     * 映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            ADD, new BQButton(ADD, "新增", "新增病案分类信息"),
            EDIT, new BQButton(EDIT, "编辑", "编辑病案分类信息"),
            DELETE, new BQButton(DELETE, "删除", "删除病案分类信息"),
            SEARCH, new BQButton(SEARCH, "查询", "查询病案分类列表"),
            SET_STATUS, new BQButton(SET_STATUS, "启用禁用", "禁用启用病案分类")
    );
}
