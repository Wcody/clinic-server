/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月29日
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
* @description <p>处方明细表 实体类</p>
* @datetime 2026-4-29 17:45
*/
@Getter
@Setter
@TableName(value = "bq_prescription_item", autoResultMap = true)
public class BqPrescriptionItemEntity extends BQIdBaseEntity {

    /**
     * 处方ID
     */
    private Integer prescId;

    /**
     * 项目类型:1西药,2中药,3中成药,101检查检验,102处置项目,103附加费
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
     * 单次用量
     */
    private String singleDosage;

    /**
     * 单次用量单位字符串
     */
    private String unit;

    /**
     * 单次用量单位Id
     */
    private Integer unitId;

    /**
     * 用法
     */
    private String useWay;

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
     * 单价单位字符串
     */
    private String priceUnit;

    /**
     * 单价单位ID
     */
    private Integer priceUnitId;

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

    /**
     * 煎药方式
     */
    private String decoWay;

    /**
     * 处方组号，相同组号回排在一起
     */
    private Integer groupNo;

    /**
     * 处方明细项的顺序，打印时会用到
     */
    private Integer sort;
}
