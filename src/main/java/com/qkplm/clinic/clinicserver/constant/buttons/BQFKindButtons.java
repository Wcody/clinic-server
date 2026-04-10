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
 * @description: 档案分类按钮
 * @datetime: 2024-07-12 09:48
 */
public class BQFKindButtons {
    public static final String ADD = "FKind:save";
    public static final String EDIT = "FKind:update";
    public static final String DELETE = "FKind:delete";
    public static final String SEARCH = "FKind:search";
    public static final String SET_STATUS = "FKind:setStatus";

    /**
     * 映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            ADD, new BQButton(ADD, "新增", "新增档案分类信息"),
            EDIT, new BQButton(EDIT, "编辑", "编辑档案分类信息"),
            DELETE, new BQButton(DELETE, "删除", "删除档案分类信息"),
            SEARCH, new BQButton(SEARCH, "查询", "查询档案分类列表"),
            SET_STATUS, new BQButton(SET_STATUS, "启用禁用", "禁用启用档案分类")
    );
}
