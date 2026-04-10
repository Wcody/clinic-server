/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月10日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>患者表（合并三方字段+本院字段，is改为has） 实体类</p>
* @datetime 2026-4-10 18:51
*/
@Getter
@Setter
@TableName(value = "tb_patient", autoResultMap = true)
public class TbPatientEntity extends BQEidBaseEntity {

    /**
     * 地址
     */
    private String address;

    /**
     * 年龄字符串 如31岁0月
     */
    private String age;

    /**
     * 年龄类型
     */
    private Byte ageType;

    /**
     * 过敏史
     */
    private String allergicHistory;

    /**
     * 档案号
     */
    private String archiveNo;

    /**
     * 城市编码
     */
    private Integer city;

    /**
     * 接触史
     */
    private String contactHistory;

    /**
     * 区县编码
     */
    private Integer district;

    /**
     * 家族史
     */
    private String familyHistory;

    /**
     * 主年龄
     */
    private Integer firstAge;

    /**
     * 性别 1男 保留独立字段
     */
    private Byte gender;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 是否过敏 0否1是 【is→has】
     */
    private Byte hasAllergy;

    /**
     * 次年龄
     */
    private Integer lastAge;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 姓名
     */
    private String name;

    /**
     * 妇产科病史
     */
    private String obstericalHistory;

    /**
     * 既往史
     */
    private String pastHistory;

    /**
     * 个人史
     */
    private String personalHistory;

    /**
     * 省份编码
     */
    private Integer province;

    /**
     * 旅行史
     */
    private String travelHistory;

    /**
     * 本院患者编号
     */
    private String patientNo;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 系统计算年龄
     */
    private Integer realAge;

    /**
     * 医保卡号
     */
    private String medicalCardNo;

    /**
     * 详细地址
     */
    private String addressDetail;

    /**
     * 状态1正常0禁用
     */
    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
