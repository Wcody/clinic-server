/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月15日
*/
package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.BqDrugSalesItemEntity;
import com.qkplm.clinic.clinicserver.mapper.BqDrugSalesItemMapper;
import com.qkplm.clinic.clinicserver.service.IBqDrugSalesItemService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Wcke
* @description <p>药房零售明细表 服务实现类</p>
* @datetime 2026-4-15
*/
@Service
public class BqDrugSalesItemServiceImpl extends BaqiServiceImpl<BqDrugSalesItemMapper, BqDrugSalesItemEntity>
        implements IBqDrugSalesItemService {

    @Override
    public List<BqDrugSalesItemEntity> listBySalesId(Integer salesId) {
        return getBaseMapper().selectBySalesId(salesId);
    }
}
