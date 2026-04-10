/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.mapper;

import com.qkplm.clinic.clinicserver.entity.BqLogEntity;
import org.apache.ibatis.annotations.Mapper;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;

/**
* @author Wcke
* @description <p>系统日志 映射器</p>
* @datetime 2024-6-26 15:53
*/
@Mapper
public interface BqLogMapper extends IBaqiMapper<BqLogEntity> {

}
