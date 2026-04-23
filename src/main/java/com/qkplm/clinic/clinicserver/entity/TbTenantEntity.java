/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月23日
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
* @description <p>系统租户 实体类</p>
* @datetime 2026-4-23 11:50
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
     * 最近登录时间
     */
    private LocalDateTime lastUpdatedTime;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
