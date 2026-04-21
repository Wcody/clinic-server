/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月22日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>使用系统的客户 实体类</p>
* @datetime 2026-4-22 2:10
*/
@Getter
@Setter
@TableName(value = "tb_customer", autoResultMap = true)
public class TbCustomerEntity extends BQEidBaseEntity {

    /**
     * 客户组ID
     */
    private String parentId;

    /**
     * 冗余字段
     */
    private String parentName;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 客户负责人
     */
    private String principal;

    /**
     * 负责人电话
     */
    private String phone;

    /**
     * 负责人邮件地址
     */
    private String email;

    /**
     * 客户地址
     */
    private String address;

    /**
     * 客户密钥
     */
    private String tenantKey;

    /**
     * [B]启用禁用状态
     */
    private Boolean status;

    /**
     * 最大可创建租户数量
     */
    private Integer maxTenantCount;

    /**
     * 租户下最大可创建用户数
     */
    private Integer maxUserCount;

    /**
     * 设备码
     */
    private String deviceCode;

    /**
     * 注册码
     */
    private String regCode;

    /**
     * 到期日
     */
    private LocalDate expireDate;

    /**
     * 描述信息
     */
    private String remark;

    /**
     * 最近登录时间
     */
    private LocalDateTime lastUpdatedTime;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * 授权类型，0临时授权（有时间限制，不是正式用户），1正式授权（有时间限制，是正式用户），2永久授权（无时间限制，是正式用户）
     */
    private Integer authType;
}
