/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.TbRoleEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

/**
* @author Wcke
* @description <p>系统角色 服务类</p>
* @datetime 2024-6-16 15:49
*/
public interface ITbRoleService extends IBaqiService<TbRoleEntity> {
    Object setStatus(String eid, Boolean status);
}
