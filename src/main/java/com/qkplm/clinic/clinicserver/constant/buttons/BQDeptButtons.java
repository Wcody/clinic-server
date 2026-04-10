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
 * @description: 部门管理
 * @datetime: 2024-06-27 21:59
 */
public class BQDeptButtons {
    public static final String ADD = "SystemDept:save";
    public static final String EDIT = "SystemDept:update";
    public static final String DELETE = "SystemDept:delete";
    public static final String SEARCH = "SystemDept:search";
    public static final String SET_STATUS = "SystemDept:setStatus";

    /**
     * 用户获取映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            ADD, new BQButton(ADD, "新增", "新建一个部门"),
            EDIT, new BQButton(EDIT, "编辑", "修改部门信息"),
            DELETE, new BQButton(DELETE, "删除", "删除部门信息"),
            SEARCH, new BQButton(SEARCH, "查询", "查询部门信息，列表查询，分页查询"),
            SET_STATUS, new BQButton(SET_STATUS, "启用禁用", "启用禁用部门")
    );
}
