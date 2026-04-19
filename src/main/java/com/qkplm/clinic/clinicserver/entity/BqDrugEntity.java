/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月19日
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
* @datetime 2026-4-19 19:15
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
     * [B]药品类型:1西药 2中药 3中成药
     */
    private Boolean type;

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
     * 处方价格
     */
    private String prescriptionPrice;

    /**
     * 采购成本价
     */
    private String purchaseCostPrice;

    /**
     * 库存
     */
    private String stock;

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
     * [B]状态:启用/禁用
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
}
