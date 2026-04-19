/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月15日
*/
package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.BqDrugSalesItemEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.util.List;

/**
* @author Wcke
* @description <p>药房零售明细表 服务类</p>
* @datetime 2026-4-15
*/
public interface IBqDrugSalesItemService extends IBaqiService<BqDrugSalesItemEntity> {

    /**
     * 根据零售主表ID查询明细列表
     */
    List<BqDrugSalesItemEntity> listBySalesId(Integer salesId);
}
