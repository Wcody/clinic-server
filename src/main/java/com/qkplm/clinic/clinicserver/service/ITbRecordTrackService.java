/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;
import com.qkplm.clinic.clinicserver.entity.TbRecordTrackEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

/**
* @author Wcke
* @description <p>病案追踪 服务类</p>
* @datetime 2024-12-17 7:29
*/
public interface ITbRecordTrackService extends IBaqiService<TbRecordTrackEntity> {
    boolean addInsertTrackBy(TbRecordEntity recordEntity);
    boolean addUpdateTrackBy(TbRecordEntity recordEntity);
    boolean addTrack(String recordId, String content);
}
