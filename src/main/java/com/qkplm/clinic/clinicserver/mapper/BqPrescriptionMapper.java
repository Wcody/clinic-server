/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月18日
*/

package com.qkplm.clinic.clinicserver.mapper;

import com.qkplm.clinic.clinicserver.entity.BqPrescriptionEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Wcke
 * @description
 *              <p>
 *              处方主表 映射器
 *              </p>
 * @datetime 2026-4-18 0:54
 */
@Mapper
public interface BqPrescriptionMapper extends IBaqiMapper<BqPrescriptionEntity> {

    @Select("SELECT ifnull(max(prescNo),0) + 1 FROM bq_prescription where createdTime between #{startDate} and #{endDate}")
    Long getNextPrescNo(String startDate, String endDate);
}
