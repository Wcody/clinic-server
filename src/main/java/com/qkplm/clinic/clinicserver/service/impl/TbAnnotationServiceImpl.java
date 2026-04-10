/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qkplm.clinic.clinicserver.entity.TbAnnotationEntity;
import com.qkplm.clinic.clinicserver.mapper.TbAnnotationMapper;
import com.qkplm.clinic.clinicserver.service.ITbAnnotationService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.security.BQSecurityUserDetails;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Wcke
* @description <p>病案附件批注信息表 服务接口类</p>
* @datetime 2024-9-21 9:1
*/
@Service
public class TbAnnotationServiceImpl extends BaqiServiceImpl<TbAnnotationMapper, TbAnnotationEntity> implements ITbAnnotationService {

    @Override
    public Boolean fix(String annotationId) {
        BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
        LambdaUpdateWrapper<TbAnnotationEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(TbAnnotationEntity::getFixer, userDetails.getAccount())
                .set(TbAnnotationEntity::getFixTime, LocalDateTime.now())
                .eq(TbAnnotationEntity::getEid, annotationId);
        return update(updateWrapper);
    }

    @Override
    public Integer getTotalPoints(String recordId) {
        return this.baseMapper.getTotalPoints(recordId);
    }

    @Override
    public List<Map<String, Integer>> getCountByRecordId(String recordId) {
        return this.baseMapper.getCountByRecordId(recordId);
    }
}
