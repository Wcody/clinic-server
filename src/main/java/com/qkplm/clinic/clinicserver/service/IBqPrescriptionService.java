/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月18日
*/

package com.qkplm.clinic.clinicserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qkplm.clinic.clinicserver.dtos.BqPrescriptionFullDto;
import com.qkplm.clinic.clinicserver.dtos.BqPrescriptionWithDiagnosisVo;
import com.qkplm.clinic.clinicserver.dtos.BqSaveMedicalOrderDto;
import com.qkplm.clinic.clinicserver.entity.BqPrescriptionEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.util.List;

/**
* @author Wcke
* @description <p>处方主表 服务类</p>
* @datetime 2026-4-18 0:54
*/
public interface IBqPrescriptionService extends IBaqiService<BqPrescriptionEntity> {

    /**
     * 统一事务保存医嘱（处方主表 + 处方明细），
     * 若患者/挂号不存在则自动新增
     */
    BqSaveMedicalOrderDto.Result saveMedicalOrder(BqSaveMedicalOrderDto dto);

    /**
     * 根据挂号ID查询该次就诊的所有处方及明细
     */
    List<BqPrescriptionFullDto> listByRegId(Integer regId);

    /**
     * 查询处方列表并关联诊断信息
     */
    List<BqPrescriptionWithDiagnosisVo> listWithDiagnosis(QueryWrapper<BqPrescriptionEntity> wrapper);
}
