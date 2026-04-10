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
public class BQParamGroupButtons {
    public static final String SEARCH = "ParamGroup:search";

    /**
     * 映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            SEARCH, new BQButton(SEARCH, "查询", "查询信息，列表查询，分页查询")
    );
}
