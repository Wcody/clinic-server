/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月13日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
* @author Wcke
* @description <p>处方明细表 实体类</p>
* @datetime 2026-4-13 23:9
*/
@Getter
@Setter
@TableName(value = "bq_prescription_item", autoResultMap = true)
public class TbPrescriptionItemEntity extends BQIdBaseEntity {

    /**
     * 处方ID
     */
    private Integer prescId;

    /**
     * 项目类型:1药品 2检查 3处置
     */
    private Byte itemType;

    /**
     * 项目ID
     */
    private Integer itemId;

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 规格
     */
    private String spec;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单次用量
     */
    private String singleDosage;

    /**
     * 用法
     */
    private String usage;

    /**
     * 频次
     */
    private String frequency;

    /**
     * 天数
     */
    private Integer days;

    /**
     * 计价总量
     */
    private BigDecimal totalNum;

    /**
     * 嘱托
     */
    private String entrust;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 金额
     */
    private BigDecimal totalPrice;

    /**
     * 行乐观锁
     */
    private Integer version;

    /**
     * [B]逻辑删除标记:0未删除 1已删除
     */
    private Boolean deleted;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * 删除人名称
     */
    private String deletedBy;
}
