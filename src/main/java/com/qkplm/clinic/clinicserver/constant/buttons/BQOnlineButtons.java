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
 * @datetime: 2024-06-28 16:50
 */
public class BQOnlineButtons {
    public static final String SEARCH = "MonitorOnline:search";
    public static final String KICK_OUT = "MonitorOnline:kickOut";

    /**
     * 用户获取映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            SEARCH, new BQButton(SEARCH, "查询", "查询在线用户信息"),
            KICK_OUT, new BQButton(KICK_OUT, "强退", "将在线用户踢下线")
    );
}
