/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月19日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>VIEW 实体类</p>
* @datetime 2026-4-19 19:15
*/
@Getter
@Setter
@TableName(value = "view_disease_full", autoResultMap = true)
public class ViewDiseaseFullEntity {

    /**
     * 疾病ID
     */
    private Long id;

    /**
     * 疾病名称
     */
    private String diseaseName;

    /**
     * 主诊科室ID
     */
    private Integer deptId;

    /**
     * 科室名称
     */
    private String deptName;

    /**
     * 疾病概述
     */
    private String summary;

    private Long syndromeCount;

    private Long expertCount;
}
