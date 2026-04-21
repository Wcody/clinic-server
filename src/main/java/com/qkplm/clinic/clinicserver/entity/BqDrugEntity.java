/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月22日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>药品管理表 实体类</p>
* @datetime 2026-4-22 2:10
*/
@Getter
@Setter
@TableName(value = "bq_drug", autoResultMap = true)
public class BqDrugEntity extends BQIdBaseEntity {

    /**
     * 药品名称
     */
    private String name;

    /**
     * 名称拼音首字母
     */
    private String pinyin;

    /**
     * 药品类型:1西药 2中药
     */
    private Byte type;

    /**
     * 药品类型名称
     */
    private String typeString;

    /**
     * 规格
     */
    private String specification;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 散卖价格
     */
    private String prescriptionPrice;

    /**
     * 散卖单位
     */
    private String prescriptionUnit;

    /**
     * 整卖价格
     */
    private String wholesalePrice;

    /**
     * 整卖单位
     */
    private String wholesaleUnit;

    /**
     * 大单位转小单位的值
     */
    private String conversionValue;

    /**
     * 采购成本价
     */
    private String purchaseCostPrice;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 生产日期
     */
    private LocalDate productionDate;

    /**
     * 生产批号
     */
    private String productionBatchNumber;

    /**
     * 有效期
     */
    private LocalDate expireDate;

    /**
     * [B]状态:1启用0/禁用
     */
    private Boolean status;

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
     * 单次用量
     */
    private String singleDosage;

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
     * 库存
     */
    private String stock;

    /**
     * 库存下限
     */
    private String minStock;

    /**
     * 初始库存单位Id
     */
    private String initialStockUnitId;

    /**
     * 批准文号
     */
    private String approvalNumber;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * 自编码
     */
    private String customCode;

    /**
     * 默认售卖方式，0整卖，1散卖
     */
    private Integer defaultSaleType;

    /**
     * 煎药方式
     */
    private String decoWay;
}
