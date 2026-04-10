/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月9日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.TbMedicalRecordEntity;
import com.qkplm.clinic.clinicserver.mapper.TbMedicalRecordMapper;
import com.qkplm.clinic.clinicserver.service.ITbMedicalRecordService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author Wcke
* @description <p>门诊就诊记录表 服务接口类</p>
* @datetime 2026-4-9 11:43
*/
@Service
public class TbMedicalRecordServiceImpl extends BaqiServiceImpl<TbMedicalRecordMapper, TbMedicalRecordEntity> implements ITbMedicalRecordService {

}
