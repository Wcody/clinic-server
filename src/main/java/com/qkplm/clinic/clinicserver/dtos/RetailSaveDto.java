/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月15日
*/
package com.qkplm.clinic.clinicserver.dtos;

import com.qkplm.clinic.clinicserver.entity.BqDrugSalesEntity;
import com.qkplm.clinic.clinicserver.entity.BqDrugSalesItemEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
* @author Wcke
* @description <p>零售保存DTO（包含销售主记录和药品明细列表）</p>
* @datetime 2026-4-15
*/
@Getter
@Setter
public class RetailSaveDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 零售主记录
     */
    private BqDrugSalesEntity sales;

    /**
     * 药品明细列表
     */
    private List<BqDrugSalesItemEntity> items;
}
