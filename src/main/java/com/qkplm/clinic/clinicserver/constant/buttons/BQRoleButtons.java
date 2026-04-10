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
 * @description: 角色管理
 * @datetime: 2024-06-27 22:00
 */
public class BQRoleButtons {
    public static final String ADD = "SystemRole:save";
    public static final String EDIT = "SystemRole:update";
    public static final String DELETE = "SystemRole:delete";
    public static final String SEARCH = "SystemRole:search";
    public static final String SET_STATUS = "SystemRole:setStatus";
    public static final String SET_AUTH = "SystemRole:setAuth";

    /**
     * 用户获取映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            ADD, new BQButton(ADD, "新增", "新增角色信息"),
            EDIT, new BQButton(EDIT, "编辑", "编辑角色信息"),
            DELETE, new BQButton(DELETE, "删除", "删除角色信息"),
            SEARCH, new BQButton(SEARCH, "查询", "查询角色列表"),
            SET_STATUS, new BQButton(SET_STATUS, "启用禁用", "禁用启用角色"),
            SET_AUTH, new BQButton(SET_AUTH, "权限", "角色权限设置")
    );
}
