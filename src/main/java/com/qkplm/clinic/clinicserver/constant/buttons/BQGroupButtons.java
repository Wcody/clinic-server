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
 * @datetime: 2024-06-28 16:41
 */
public class BQGroupButtons {
    public static final String ADD = "TenantGroup:save";
    public static final String EDIT = "TenantGroup:update";
    public static final String DELETE = "TenantGroup:delete";
    public static final String SEARCH = "TenantGroup:search";
    public static final String SET_STATUS = "TenantGroup:setStatus";

    /**
     * 用户获取映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            ADD, new BQButton(ADD, "新增", "新建一个客户组"),
            EDIT, new BQButton(EDIT, "编辑", "修改客户组信息"),
            DELETE, new BQButton(DELETE, "删除", "删除客户组信息"),
            SEARCH, new BQButton(SEARCH, "查询", "查询客户组信息，列表查询，分页查询"),
            SET_STATUS, new BQButton(SET_STATUS, "启用禁用", "启用禁用客户组")
    );
}
