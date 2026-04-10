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
 * @description: 菜单管理
 * @datetime: 2024-06-27 21:46
 */
public class BQMenuButtons {
    public static final String ADD = "SystemMenu:save";
    public static final String EDIT = "SystemMenu:update";
    public static final String DELETE = "SystemMenu:delete";
    public static final String SEARCH = "SystemMenu:search";
    public static final String SET_STATUS = "SystemMenu:setStatus";
    public static final String RELOAD_RESOURCE = "SystemMenu:reloadResource";

    /**
     * 用户获取映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            ADD, new BQButton(ADD, "新增", "新增一个菜单"),
            EDIT, new BQButton(EDIT, "编辑", "编辑菜单信息"),
            DELETE, new BQButton(DELETE, "删除", "删除菜单信息"),
            SEARCH, new BQButton(SEARCH, "查询", "查询菜单信息"),
            SET_STATUS, new BQButton(SET_STATUS, "启用禁用", "启用禁用菜单"),
            RELOAD_RESOURCE, new BQButton(RELOAD_RESOURCE, "重新加载菜单", "重新生成菜单下的按钮配置")
    );
}
