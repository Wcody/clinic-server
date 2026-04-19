/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月18日
*/

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.BqMedicalRecordEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

/**
* @author Wcke
* @description <p>门诊就诊记录表 服务类</p>
* @datetime 2026-4-18 0:54
*/
public interface IBqMedicalRecordService extends IBaqiService<BqMedicalRecordEntity> {

    /**
     * 根据挂号ID查询该次就诊的病历（一个挂号对应一份病历）
     */
    BqMedicalRecordEntity getByRegId(Integer regId);
}
