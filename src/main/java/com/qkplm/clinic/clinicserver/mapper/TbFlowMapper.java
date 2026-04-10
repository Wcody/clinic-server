/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.mapper;

import com.qkplm.clinic.clinicserver.entity.TbFlowEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Wcke
* @description <p>审批工作流 映射器</p>
* @datetime 2024-9-9 15:47
*/
@Mapper
public interface TbFlowMapper extends IBaqiMapper<TbFlowEntity> {

}
