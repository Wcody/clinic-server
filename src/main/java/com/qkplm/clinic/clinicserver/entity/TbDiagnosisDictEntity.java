/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月18日
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
* @description <p>诊断字典表 实体类</p>
* @datetime 2026-4-18 0:54
*/
@Getter
@Setter
@TableName(value = "tb_diagnosis_dict", autoResultMap = true)
public class TbDiagnosisDictEntity extends BQEidBaseEntity {

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * ICD编码
     */
    private String diagnosisCode;

    /**
     * 诊断名称
     */
    private String diagnosisName;

    /**
     * 拼音码
     */
    private String pinyin;

    private Byte status;
}
