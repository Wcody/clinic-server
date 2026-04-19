/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月18日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>挂号表 实体类</p>
* @datetime 2026-4-18 0:54
*/
@Getter
@Setter
@TableName(value = "tb_registration", autoResultMap = true)
public class TbRegistrationEntity extends BQEidBaseEntity {

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * 挂号单号
     */
    private String regNo;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 医生ID
     */
    private Integer doctorId;

    /**
     * 就诊日期
     */
    private LocalDate regDate;

    /**
     * 上午/下午
     */
    private String timeSlot;

    /**
     * 1普通 2急诊
     */
    private Byte regType;

    /**
     * 挂号费
     */
    private BigDecimal fee;

    /**
     * 1已挂号 2就诊中 3已完成 4已取消
     */
    private Byte status;

    private LocalDateTime createTime;
}
