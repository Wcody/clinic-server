/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月11日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>病案追踪 实体类</p>
* @datetime 2026-4-11 9:28
*/
@Getter
@Setter
@TableName(value = "tb_record_track", autoResultMap = true)
public class TbRecordTrackEntity extends BQEidBaseEntity {

    /**
     * 病案ID
     */
    private String recordId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 追踪内容
     */
    private String content;

    /**
     * 备注
     */
    private String remark;

    /**
     * 跟踪类型0入库1护士2医生3终末4医生驳回5终末驳回
     */
    private Integer trackKind;
}
