/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.TbKindEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.util.HashMap;
import java.util.List;

/**
* @author Wcke
* @description <p>档案分类 服务类</p>
* @datetime 2024-7-12 9:43
*/
public interface ITbKindService extends IBaqiService<TbKindEntity> {
    Object setStatus(String eid, Boolean status);
    List<TbKindEntity> listQt(HashMap<String, Object> params);
}
