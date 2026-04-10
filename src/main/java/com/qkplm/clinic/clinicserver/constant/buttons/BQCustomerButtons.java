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
 * @description:
 * @datetime: 2024-07-23 11:29
 */
public class BQCustomerButtons {
    public static final String ADD = "Customer:save";
    public static final String EDIT = "Customer:update";
    public static final String DELETE = "Customer:delete";
    public static final String SEARCH = "Customer:search";
    public static final String SET_STATUS = "Customer:setStatus";
    public static final String SET_ADMIN = "Customer:setAdmin";
    public static final String SET_MENU = "Customer:setMenu";

    /**
     * 获取映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            ADD, new BQButton(ADD, "新增", "新增客户信息"),
            EDIT, new BQButton(EDIT, "编辑", "编辑客户信息"),
            DELETE, new BQButton(DELETE, "删除", "删除客户信息"),
            SEARCH, new BQButton(SEARCH, "查询", "查询客户信息"),
            SET_STATUS, new BQButton(SET_STATUS, "启用禁用", "启用禁用客户"),
            SET_ADMIN, new BQButton(SET_ADMIN, "设置管理员", "设置客户的管理员，用于首次登录初始化系统"),
            SET_MENU, new BQButton(SET_MENU, "设置可用菜单", "设置客户可以使用的菜单，没有授权的无法使用")
    );
}
