/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.TbPatientEntity;
import com.qkplm.clinic.clinicserver.mapper.TbPatientMapper;
import com.qkplm.clinic.clinicserver.service.ITbPatientService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author Wcke
* @description <p>患者信息表 服务接口类</p>
* @datetime 2024-9-21 9:1
*/
@Service
public class TbPatientServiceImpl extends BaqiServiceImpl<TbPatientMapper, TbPatientEntity> implements ITbPatientService {

}
