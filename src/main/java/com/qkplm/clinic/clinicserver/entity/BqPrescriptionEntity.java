/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月23日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>处方主表 实体类</p>
* @datetime 2026-4-23 11:50
*/
@Getter
@Setter
@TableName(value = "bq_prescription", autoResultMap = true)
public class BqPrescriptionEntity extends BQIdBaseEntity {

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
     * 处方类型:1西药 2中药 3检查 4处置
     */
    private Integer prescType;

    /**
     * 组号
     */
    private String groupNo;

    /**
     * 处方总价
     */
    private BigDecimal totalPrice;

    /**
     * 用法类型ID，中药处方才用
     */
    private Integer usageType;

    /**
     * 频率ID，中药处方才用
     */
    private Integer frequence;

    /**
     * 剂数，中药处方才用
     */
    private Integer doseAmount;

    /**
     * 医嘱/嘱托，中药处方才用
     */
    private String recommendation;

    private Integer days;

    /**
     * 状态:1已开 2已缴费 3已发药 4作废
     */
    private Byte status;

    /**
     * 行乐观锁
     */
    private Integer version;

    /**
     * [B]逻辑删除标记:0未删除 1已删除
     */
    private Boolean deleted;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * 删除人名称
     */
    private String deletedBy;

    private Integer decoWay;
}
