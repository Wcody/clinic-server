/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月29日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>系统租户 实体类</p>
* @datetime 2026-4-29 17:45
*/
@Getter
@Setter
@TableName(value = "tb_tenant", autoResultMap = true)
public class TbTenantEntity extends BQEidBaseEntity {

    /**
     * 租组ID
     */
    private String parentId;

    /**
     * 冗余字段
     */
    private String parentName;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 租户负责人
     */
    private String principal;

    /**
     * 租户电话
     */
    private String phone;

    /**
     * 租户邮件地址
     */
    private String email;

    /**
     * 租户地址
     */
    private String address;

    /**
     * 租户密钥
     */
    private String tenantKey;

    /**
     * [B]启用禁用状态
     */
    private Boolean status;

    /**
     * 授权类型，0临时授权，1正式授权，2永久授权
     */
    private Integer authType;

    /**
     * 最大可创建用户数量
     */
    private Integer maxUserCount;

    /**
     * 设备码
     */
    private String deviceCode;

    /**
     * 到期日
     */
    private LocalDateTime expireDate;

    /**
     * 描述信息
     */
    private String remark;


    /**
     * 初始管理员账号
     */
    @TableField(exist = false)
    private String adminAccount;

    /**
     * 初始管理员密码
     */
    @TableField(exist = false)
    private String adminPassword;

    /**
     * 初始管理员姓名
     */
    @TableField(exist = false)
    private String adminName;

    /**
     * 初始管理员手机号
     */
    @TableField(exist = false)
    private String adminPhone;

    /**
     * 初始管理员邮箱
     */
    @TableField(exist = false)
    private String adminEmail;

    /**
     * 当前用户数
     */
    @TableField(exist = false)
    private Integer currentUserCount;

    /**
     * 管理员数
     */
    @TableField(exist = false)
    private Integer adminCount;

    /**
     * 授权菜单数
     */
    @TableField(exist = false)
    private Integer menuCount;

    /**
     * 最近登录时间
     */
    private LocalDateTime lastUpdatedTime;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
