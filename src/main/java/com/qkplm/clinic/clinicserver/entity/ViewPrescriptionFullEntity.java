/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月26日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>VIEW 实体类</p>
* @datetime 2026-4-26 7:17
*/
@Getter
@Setter
@TableName(value = "view_prescription_full", autoResultMap = true)
public class ViewPrescriptionFullEntity {

    /**
     * 基本方ID
     */
    private Long id;

    /**
     * 方剂名称
     */
    private String prescName;

    /**
     * 出处(如《伤寒论》)
     */
    private String source;

    /**
     * 功效
     */
    private String efficacy;

    /**
     * 主治
     */
    private String indications;

    private String ingredients;
}
