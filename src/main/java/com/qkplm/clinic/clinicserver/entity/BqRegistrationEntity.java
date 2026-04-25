/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月26日
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
* @description <p>挂号列表(挂号记录)表 实体类</p>
* @datetime 2026-4-26 7:17
*/
@Getter
@Setter
@TableName(value = "bq_registration", autoResultMap = true)
public class BqRegistrationEntity extends BQIdBaseEntity {

    /**
     * [B]行乐观锁
     */
    private Boolean version;

    /**
     * [B]逻辑删除标记
     */
    private Boolean deleted;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * 患者姓名
     */
    private String patient;

    /**
     * 性别
     */
    private String gender;

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
     * 挂号号
     */
    private Integer registrationNo;

    /**
     * 科室
     */
    private String department;

    /**
     * 医生
     */
    private String doctor;

    /**
     * 诊所ID
     */
    private Integer clinic;

    /**
     * 总金额
     */
    private BigDecimal totalPrice;

    /**
     * 挂号类型ID
     */
    private Integer registrationType;

    /**
     * 门诊类型:自费门诊/医保等
     */
    private String outpatientType;

    /**
     * [B]是否初诊:1是 0否
     */
    private Boolean isFirstVisit;

    /**
     * 下单时间
     */
    private LocalDateTime orderTime;

    /**
     * 就诊状态:待接诊/已接诊/已退号等
     */
    private String status;

    /**
     * 更新人
     */
    private String deletedBy;

    /**
     * 患者Id
     */
    private Integer patientId;

    /**
     * 年龄字符串：3年2月
     */
    private String age;

    /**
     * 收费状态:未缴费/已缴费/已退费等等
     */
    private String statusFee;
}
