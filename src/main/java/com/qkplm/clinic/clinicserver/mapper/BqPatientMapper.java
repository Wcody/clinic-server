/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月13日
*/

package com.qkplm.clinic.clinicserver.mapper;

import com.qkplm.clinic.clinicserver.entity.BqPatientEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Wcke
* @description <p>患者信息表 映射器</p>
* @datetime 2026-4-13 11:55
*/
@Mapper
public interface BqPatientMapper extends IBaqiMapper<BqPatientEntity> {

}
