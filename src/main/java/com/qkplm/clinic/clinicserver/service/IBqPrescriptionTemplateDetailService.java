/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/

package com.qkplm.clinic.clinicserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qkplm.clinic.clinicserver.dtos.BqPrescriptionTemplateDetailDto;
import com.qkplm.clinic.clinicserver.entity.BqPrescriptionTemplateDetailEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.util.List;

/**
* @author Wcke
* @description <p>处方模板明细表 服务类</p>
* @datetime 2026-4-14 12:56
*/
public interface IBqPrescriptionTemplateDetailService extends IBaqiService<BqPrescriptionTemplateDetailEntity> {

    List<BqPrescriptionTemplateDetailDto> listWithPrice(
            Page<BqPrescriptionTemplateDetailEntity> page,
            QueryWrapper<BqPrescriptionTemplateDetailEntity> wrapper);
}
