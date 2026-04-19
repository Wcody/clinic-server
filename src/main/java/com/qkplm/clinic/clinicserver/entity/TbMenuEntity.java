/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月19日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>系统菜单 实体类</p>
* @datetime 2026-4-19 19:15
*/
@Getter
@Setter
@TableName(value = "tb_menu", autoResultMap = true)
public class TbMenuEntity extends BQEidBaseEntity {

    /**
     * 菜单类型，0代表菜单、1代表iframe、2代表外链、3代表按钮
     */
    private Integer menuType;

    /**
     * 上级菜单ID
     */
    private String parentId;

    /**
     * 路由名称
     */
    private String title;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 菜单排序
     */
    private Integer orderValue;

    /**
     * 路由重定向
     */
    private String redirect;

    /**
     * 菜单左侧图标
     */
    private String icon;

    /**
     * 菜单右侧图标
     */
    private String extraIcon;

    /**
     * 进场动画
     */
    private String enterTransition;

    /**
     * 离场动画
     */
    private String leaveTransition;

    /**
     * 菜单激活
     */
    private String activePath;

    /**
     * 可操作权限
     */
    private String auths;

    /**
     * 链接地址
     */
    private String frameSrc;

    /**
     * [B]加载动画
     */
    private Boolean frameLoading;

    /**
     * [B]是否缓存
     */
    private Boolean keepAlive;

    /**
     * [B]是否隐藏标签页
     */
    private Boolean hiddenTag;

    /**
     * [B]是否固定标签页
     */
    private Boolean fixedTag;

    /**
     * [B]是否显示菜单
     */
    private Boolean showLink;

    /**
     * [B]是否显示父级菜单
     */
    private Boolean showParent;

    /**
     * 可访问角色
     */
    private String roles;

    private String remark;

    /**
     * [B]菜单状态
     */
    private Boolean status;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
