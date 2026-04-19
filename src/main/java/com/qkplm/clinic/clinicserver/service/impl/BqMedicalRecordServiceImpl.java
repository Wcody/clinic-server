/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月18日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qkplm.clinic.clinicserver.entity.BqMedicalRecordEntity;
import com.qkplm.clinic.clinicserver.mapper.BqMedicalRecordMapper;
import com.qkplm.clinic.clinicserver.service.IBqMedicalRecordService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author Wcke
* @description <p>门诊就诊记录表 服务接口类</p>
* @datetime 2026-4-18 0:54
*/
@Service
public class BqMedicalRecordServiceImpl extends BaqiServiceImpl<BqMedicalRecordMapper, BqMedicalRecordEntity> implements IBqMedicalRecordService {

    @Override
    public BqMedicalRecordEntity getByRegId(Integer regId) {
        return getOne(new LambdaQueryWrapper<BqMedicalRecordEntity>()
                .eq(BqMedicalRecordEntity::getRegId, regId)
                .last("LIMIT 1"));
    }
}
