/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月15日
*/
package com.qkplm.clinic.clinicserver.mapper;

import com.qkplm.clinic.clinicserver.entity.BqDrugSalesItemEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Wcke
* @description <p>药房零售明细表 映射器</p>
* @datetime 2026-4-15
*/
@Mapper
public interface BqDrugSalesItemMapper extends IBaqiMapper<BqDrugSalesItemEntity> {

    /**
     * 根据零售主表ID查询明细列表
     */
    @Select("SELECT * FROM bq_drug_sales_item WHERE sales_id = #{salesId} AND deleted = 0")
    List<BqDrugSalesItemEntity> selectBySalesId(Integer salesId);
}
