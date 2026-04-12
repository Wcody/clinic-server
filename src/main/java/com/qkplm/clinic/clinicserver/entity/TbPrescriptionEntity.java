/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月11日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>处方主表 实体类</p>
* @datetime 2026-4-11 9:28
*/
@Getter
@Setter
@TableName(value = "tb_prescription", autoResultMap = true)
public class TbPrescriptionEntity extends BQEidBaseEntity {

    /**
     * 处方单号
     */
    private String prescNo;

    /**
     * 就诊记录ID
     */
    private Integer recordId;

    /**
     * 挂号ID（可为空）
     */
    private Integer regId;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 开方医生ID
     */
    private Integer doctorId;

    /**
     * 1西药 2中药 3检查 4处置
     */
    private Byte prescType;

    /**
     * 组号
     */
    private String groupNo;

    private BigDecimal totalPrice;

    /**
     * 1已开 2已缴费 3已发药 4作废
     */
    private Byte status;

    private LocalDateTime createTime;
}
