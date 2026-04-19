/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
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
* @description <p>挂号列表(挂号记录)表 实体类</p>
* @datetime 2026-4-14 18:14
*/
@Getter
@Setter
@TableName(value = "bq_egistration", autoResultMap = true)
public class BqEgistrationEntity extends BQIdBaseEntity {

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
    private Integer ageFirst;

    /**
     * 末次年龄
     */
    private Integer ageLast;

    /**
     * [B]年龄类型
     */
    private Boolean ageType;

    /**
     * 挂号号
     */
    private String registrationNo;

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
     * 状态:待接诊/已接诊等
     */
    private String status;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除人名称,字符串,长度256
     */
    private String deletedBy;
}
