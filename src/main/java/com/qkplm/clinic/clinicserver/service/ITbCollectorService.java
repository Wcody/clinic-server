/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.TbCollectorEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

/**
* @author Wcke
* @description <p>采集器 服务类</p>
* @datetime 2025-1-2 22:26
*/
public interface ITbCollectorService extends IBaqiService<TbCollectorEntity> {
    Object setStatus(String eid, boolean status);
    TbCollectorEntity getByNameAndDeviceId(String collectorName, String deviceId);
}
