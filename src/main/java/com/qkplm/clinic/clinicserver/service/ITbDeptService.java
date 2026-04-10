/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.TbDeptEntity;
import com.qkplm.clinic.clinicserver.entity.TbUserEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.util.List;

/**
* @author Wcke
* @description <p>部门 服务类</p>
* @datetime 2024-6-21 17:6
*/
public interface ITbDeptService extends IBaqiService<TbDeptEntity> {
    Object setStatus(String eid, Boolean status);
    List<TbUserEntity> getDeptInfo(List<TbUserEntity> users);
}
