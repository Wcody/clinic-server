/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月22日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>药房零售表 实体类</p>
* @datetime 2026-4-22 2:10
*/
@Getter
@Setter
@TableName(value = "bq_drug_sales", autoResultMap = true)
public class BqDrugSalesEntity extends BQIdBaseEntity {

    /**
     * 金额
     */
    private String amount;

    /**
     * 实收金额
     */
    private String actualAmount;

    /**
     * 操作人
     */
    private String operatorPerson;

    /**
     * 创建时间
     */
    private LocalDateTime created;

    /**
     * [B]状态:1已收费 0未收费
     */
    private Boolean status;

    /**
     * 状态说明
     */
    private String statusRemark;

    /**
     * 患者ID（关联 bq_patient.id）
     */
    private Integer patientId;

    private String patientName;

    /**
     * 初诊年龄
     */
    private Integer firstAge;

    /**
     * 末次年龄
     */
    private Integer lastAge;

    /**
     * 年龄类型
     */
    private Byte ageType;

    /**
     * 性别
     */
    private String age;

    /**
     * 行乐观锁,整数,非空,默认0
     */
    private Integer version;

    /**
     * [B]逻辑删除标记,0表示false,1表示true
     */
    private Boolean deleted;

    /**
     * 删除人名称,字符串,长度256
     */
    private String deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
