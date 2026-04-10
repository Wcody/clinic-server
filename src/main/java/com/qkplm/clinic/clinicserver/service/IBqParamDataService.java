/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qkplm.clinic.clinicserver.entity.BqParamDataEntity;
import com.qkplm.clinic.clinicserver.entity.BqParamItemEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

/**
* @author Wcke
* @description <p>系统参数数据表 服务类</p>
* @datetime 2024-9-21 9:1
*/
public interface IBqParamDataService extends IBaqiService<BqParamDataEntity> {
    ObjectNode getRealParamData(Integer pid, Integer id, ObjectNode defaultValue);
    BqParamItemEntity getRealParamItem(Integer pid, Integer id, BqParamItemEntity defaultItem);
    BqParamDataEntity getParamDataEntity(Integer paramId, String userId);
}
