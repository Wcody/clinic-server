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
 * @description: 系统日志
 * @datetime: 2024-06-28 11:23
 */
public class BQLogSystemButtons {
    public static final String DELETE = "MonitorSysLog:delete";
    public static final String SEARCH = "MonitorSysLog:search";
    public static final String CLEAR = "MonitorSysLog:clear";

    /**
     * 用户获取映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            DELETE, new BQButton(DELETE, "删除", "删除系统日志"),
            SEARCH, new BQButton(SEARCH, "查询", "查询系统日志"),
            CLEAR, new BQButton(CLEAR, "清空", "清空系统日志")
    );
}
