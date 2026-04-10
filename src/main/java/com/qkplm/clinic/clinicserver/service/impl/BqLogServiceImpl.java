/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.BqLogEntity;
import com.qkplm.clinic.clinicserver.mapper.BqLogMapper;
import com.qkplm.clinic.clinicserver.service.IBqLogService;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;

/**
* @author Wcke
* @description <p>系统日志 服务接口类</p>
* @datetime 2024-6-26 15:53
*/
@Service
public class BqLogServiceImpl extends BaqiServiceImpl<BqLogMapper, BqLogEntity> implements IBqLogService {

}
