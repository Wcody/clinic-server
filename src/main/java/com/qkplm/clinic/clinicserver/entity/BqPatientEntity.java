/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月19日
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
* @description <p>患者信息表 实体类</p>
* @datetime 2026-4-19 19:15
*/
@Getter
@Setter
@TableName(value = "bq_patient", autoResultMap = true)
public class BqPatientEntity extends BQIdBaseEntity {

    /**
     * 患者姓名
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private String age;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 档案号
     */
    private String archiveNo;

    /**
     * 省份ID
     */
    private Integer province;

    /**
     * 城市ID
     */
    private Integer city;

    /**
     * 区县ID
     */
    private Integer district;

    /**
     * 详细地址
     */
    private String address;

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
    private Integer ageType;

    /**
     * 是否过敏:1是 0否
     */
    private Boolean isAllergy;

    /**
     * 过敏史
     */
    private String allergicHistory;

    /**
     * 既往史
     */
    private String pastHistory;

    /**
     * 个人史
     */
    private String personalHistory;

    /**
     * 家族史
     */
    private String familyHistory;

    /**
     * 婚育史
     */
    private String obstericalHistory;

    /**
     * 旅行史
     */
    private String travelHistory;

    /**
     * 接触史
     */
    private String contactHistory;

    /**
     * 行乐观锁
     */
    private Integer version;

    /**
     * 逻辑删除标记:1已删除 0未删除
     */
    private Boolean deleted;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * 删除人
     */
    private String deletedBy;
}
