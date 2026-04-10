/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Select;
import com.qkplm.clinic.clinicserver.dtos.BQRecordDto;
import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Wcke
* @description <p>病案表 映射器</p>
* @datetime 2024-9-21 9:1
*/
@Mapper
public interface TbRecordMapper extends IBaqiMapper<TbRecordEntity> {
    @Select("${sql}")
    List<BQRecordDto> listWz(String sql);
}
