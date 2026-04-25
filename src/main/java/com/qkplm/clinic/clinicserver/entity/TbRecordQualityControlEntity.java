/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月26日
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
* @description <p>病案质控记录 实体类</p>
* @datetime 2026-4-26 7:17
*/
@Getter
@Setter
@TableName(value = "tb_record_quality_control", autoResultMap = true)
public class TbRecordQualityControlEntity extends BQEidBaseEntity {

    /**
     * 档案、病案记录ID
     */
    private String recordId;

    /**
     * 需要质控的角色ID
     */
    private String roleId;

    /**
     * 该角色下实际质控人ID
     */
    private String userId;

    /**
     * 该角色下实际质控人名称
     */
    private String account;

    /**
     * 质控类型，0常规质控，1终末质控
     */
    private Integer kind;

    /**
     * 质控顺序
     */
    private Integer orderValue;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * [B]启用禁用, 0禁用、1启用
     */
    private Boolean status;
}
