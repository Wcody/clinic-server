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
 * @description: 病案分类识别关键字按钮
 * @datetime: 2024-07-12 09:56
 */
public class BQMKindKeysButtons {
    public static final String ADD = "MKindKeys:save";
    public static final String EDIT = "MKindKeys:update";
    public static final String DELETE = "MKindKeys:delete";
    public static final String SEARCH = "MKindKeys:search";
    public static final String UPLOAD_TEMP = "MKindKeys:uploadTemp";
    public static final String DELETE_TEMP = "MKindKeys:deleteTemp";

    /**
     * 映射信息
     */
    private static final Map<String, BQButton> map = Map.of(
            ADD, new BQButton(ADD, "新增", "新增识别关键字信息"),
            EDIT, new BQButton(EDIT, "编辑", "编辑识别关键字信息"),
            DELETE, new BQButton(DELETE, "删除", "删除识别关键字信息"),
            SEARCH, new BQButton(SEARCH, "查询", "查询识别关键字信息列表"),
            UPLOAD_TEMP, new BQButton(UPLOAD_TEMP, "上传模板", "上传病案分类的图片模板"),
            DELETE_TEMP, new BQButton(DELETE_TEMP, "删除模板", "删除病案分类的图片模板")
    );
}
