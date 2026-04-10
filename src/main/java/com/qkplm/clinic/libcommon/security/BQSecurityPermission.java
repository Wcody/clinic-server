/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author: Wcke
 * @description: 路径与角色权限
 * @datetime: 2024-06-16 18:26
 */
@Data
@Builder
@AllArgsConstructor
public class BQSecurityPermission {
    /**
     * 鉴权配置: ?替换成*，存储整个系统的权限配置，权限更新时，重新加载
     * /ams/api/?/user/save/?,user.add
     * /ams/api/?/user/delete/?,user.delete
     * /ams/api/?/user/update/?,user.update
     * /ams/api/?/user/get/*?,user.get
     */
    public String getPattern() {
        return path.replace(String.format("/%s/", version), "/*/");
    };

    /**
     * 映射路径
     */
    private String path;

    /**
     * 类名
     */
    private String clazz;

    /**
     * 方法名
     */
    private String method;

    /**
     * 该接口的权限标识
     */
    private String auth;

    /**
     * 可以访问该接口的按钮列表
     */
    private List<String> buttons;

    /**
     * 版本
     */
    private String version = "v1";
}
