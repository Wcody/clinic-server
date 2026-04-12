/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月11日
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
* @description <p>门诊就诊记录表 实体类</p>
* @datetime 2026-4-11 9:28
*/
@Getter
@Setter
@TableName(value = "tb_medical_record", autoResultMap = true)
public class TbMedicalRecordEntity extends BQEidBaseEntity {

    /**
     * 挂号ID（无挂号为空）
     */
    private Integer regId;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 接诊医生ID
     */
    private Integer doctorId;

    /**
     * 主诉
     */
    private String chiefComplaint;

    /**
     * 现病史
     */
    private String presentIllness;

    /**
     * 既往史
     */
    private String pastHistory;

    /**
     * 体格检查
     */
    private String physicalExam;

    /**
     * 诊断结果
     */
    private String diagnosis;

    /**
     * 诊断编码ID串
     */
    private String diagnosisIds;

    /**
     * 医嘱
     */
    private String advice;

    /**
     * 就诊时间
     */
    private LocalDateTime seeTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
