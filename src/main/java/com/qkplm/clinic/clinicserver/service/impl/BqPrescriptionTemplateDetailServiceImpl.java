/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qkplm.clinic.clinicserver.dtos.BqPrescriptionTemplateDetailDto;
import com.qkplm.clinic.clinicserver.entity.BqDrugEntity;
import com.qkplm.clinic.clinicserver.entity.BqPrescriptionTemplateDetailEntity;
import com.qkplm.clinic.clinicserver.mapper.BqPrescriptionTemplateDetailMapper;
import com.qkplm.clinic.clinicserver.service.IBqDrugService;
import com.qkplm.clinic.clinicserver.service.IBqPrescriptionTemplateDetailService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author Wcke
* @description <p>处方模板明细表 服务接口类</p>
* @datetime 2026-4-14 12:56
*/
@Service
public class BqPrescriptionTemplateDetailServiceImpl extends BaqiServiceImpl<BqPrescriptionTemplateDetailMapper, BqPrescriptionTemplateDetailEntity> implements IBqPrescriptionTemplateDetailService {

    private final IBqDrugService drugService;

    public BqPrescriptionTemplateDetailServiceImpl(@Lazy IBqDrugService drugService) {
        this.drugService = drugService;
    }

    @Override
    public List<BqPrescriptionTemplateDetailDto> listWithPrice(
            Page<BqPrescriptionTemplateDetailEntity> page,
            QueryWrapper<BqPrescriptionTemplateDetailEntity> wrapper) {

        List<BqPrescriptionTemplateDetailEntity> details = this.list(page, wrapper);
        if (details == null || details.isEmpty()) {
            return Collections.emptyList();
        }

        // 收集所有 drugId，批量查询药品价格
        List<Integer> drugIds = details.stream()
                .map(BqPrescriptionTemplateDetailEntity::getDrugId)
                .filter(Objects::nonNull)
                .distinct()
                .map(Long::intValue)
                .collect(Collectors.toList());

        Map<Long, String> priceMap = new HashMap<>();
        if (!drugIds.isEmpty()) {
            List<BqDrugEntity> drugs = drugService.listByIds(drugIds);
            for (BqDrugEntity drug : drugs) {
                if (drug.getId() != null) {
                    priceMap.put(Long.valueOf(drug.getId()), drug.getPrescriptionPrice());
                }
            }
        }

        return details.stream().map(d -> {
            BqPrescriptionTemplateDetailDto dto = new BqPrescriptionTemplateDetailDto();
            BeanUtils.copyProperties(d, dto);
            dto.setPrice(d.getDrugId() != null ? priceMap.get(d.getDrugId()) : null);
            return dto;
        }).collect(Collectors.toList());
    }
}
