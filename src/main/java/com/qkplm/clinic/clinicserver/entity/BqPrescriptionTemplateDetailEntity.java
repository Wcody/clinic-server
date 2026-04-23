/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月23日
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
* @description <p>处方模板明细表 实体类</p>
* @datetime 2026-4-23 11:50
*/
@Getter
@Setter
@TableName(value = "bq_prescription_template_detail", autoResultMap = true)
public class BqPrescriptionTemplateDetailEntity extends BQIdBaseEntity {

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 药品ID
     */
    private Long drugId;

    /**
     * 药品名称
     */
    private String drugName;

    /**
     * 计价总量
     */
    private String quantity;

    /**
     * 计价单位ID
     */
    private Integer quantityUnit;

    /**
     * 煎煮类型ID，中药才用到
     */
    private Integer cookingType;

    /**
     * 天数
     */
    private Integer days;

    /**
     * 组号（用于分组显示）
     */
    private Integer groupNo;

    /**
     * 单次用量
     */
    private String singleUsageAmount;

    /**
     * 单次用量单位ID
     */
    private Integer singleUsageUnit;

    /**
     * 规格
     */
    private String specification;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

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

    /**
     * 用法类型ID
     */
    private Integer usageType;

    /**
     * 医嘱/嘱托
     */
    private String recommendation;
}
