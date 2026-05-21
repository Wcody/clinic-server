/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月29日
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
* @description <p>系统角色 实体类</p>
* @datetime 2026-4-29 17:45
*/
@Getter
@Setter
@TableName(value = "tb_role", autoResultMap = true)
public class TbRoleEntity extends BQEidBaseEntity {

    /**
     * 角色标识
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 密级，1公开，2内部，3秘密，4机密，5绝密
     */
    private Integer secretLevel;

    /**
     * [B]启用禁用状态
     */
    private Boolean status;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
    /**
     * [B]租户初始化数据
     */
    private Boolean tenantInitData;
}
