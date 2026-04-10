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
 * @description: 用户管理
 * @datetime: 2024-06-27 21:59
 */
public class BQUserButtons {
    public static final String ADD = "SystemUser:save";
    public static final String EDIT = "SystemUser:update";
    public static final String DELETE = "SystemUser:delete";
    public static final String SEARCH = "SystemUser:search";
    public static final String SET_STATUS = "SystemUser:setStatus";
    public static final String RESET_PASS = "SystemUser:resetPass";
    public static final String SET_ROLES = "SystemUser:setRoles";

    /**
     * 用户获取映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            ADD, new BQButton(ADD, "新增", "新增用户信息"),
            EDIT, new BQButton(EDIT, "编辑", "编辑用户信息"),
            DELETE, new BQButton(DELETE, "删除", "删除用户信息"),
            SEARCH, new BQButton(SEARCH, "查询", "查询用户信息"),
            SET_STATUS, new BQButton(SET_STATUS, "启用禁用", "启用禁用用户"),
            RESET_PASS, new BQButton(RESET_PASS, "重置密码", "将用户密码重置，管理员操作"),
            SET_ROLES, new BQButton(SET_ROLES, "分配角色", "给用户分配角色，管理员操作")
    );
}
