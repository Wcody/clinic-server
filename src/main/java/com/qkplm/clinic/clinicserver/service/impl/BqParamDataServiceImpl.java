/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qkplm.clinic.clinicserver.constant.BqDataConst;
import com.qkplm.clinic.clinicserver.entity.BqParamDataEntity;
import com.qkplm.clinic.clinicserver.entity.BqParamItemEntity;
import com.qkplm.clinic.clinicserver.mapper.BqParamDataMapper;
import com.qkplm.clinic.clinicserver.service.IBqParamDataService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;

import java.util.Objects;

/**
* @author Wcke
* @description <p>系统参数数据表 服务接口类</p>
* @datetime 2024-9-21 9:1
*/
@Service
public class BqParamDataServiceImpl extends BaqiServiceImpl<BqParamDataMapper, BqParamDataEntity> implements IBqParamDataService {
    @Override
    public ObjectNode getRealParamData(Integer pid, Integer id, ObjectNode defaultValue) {
        String userId = "0";
        if (BqDataConst.MINE_PID == pid) {
            userId = BQRequestContextHolderUtils.getUserDetails().getEid();
        }
        BqParamDataEntity paramData = getParamDataEntity(id, userId);
        if (paramData == null) {
            return defaultValue;
        } else {
            return paramData.getParamValue();
        }
    }

    @Override
    public BqParamItemEntity getRealParamItem(Integer pid, Integer id, BqParamItemEntity defaultItem) {
        String userId = "0";
        if (BqDataConst.MINE_PID == pid) {
            userId = BQRequestContextHolderUtils.getUserDetails().getEid();
        }
        BqParamDataEntity paramData = getParamDataEntity(id, userId);
        if (Objects.nonNull(paramData)) {
            defaultItem.setParamValue(paramData.getParamValue());
            defaultItem.setCreatedBy(paramData.getCreatedBy());
            defaultItem.setCreatedTime(paramData.getCreatedTime());
            defaultItem.setUpdatedBy(paramData.getUpdatedBy());
            defaultItem.setUpdatedTime(paramData.getUpdatedTime());
        }
        return defaultItem;
    }

    @Override
    public BqParamDataEntity getParamDataEntity(Integer paramId, String userId) {
        LambdaQueryWrapper<BqParamDataEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BqParamDataEntity::getParamId, paramId).eq(BqParamDataEntity::getUserId, userId);
        queryWrapper.eq(BqParamDataEntity::getTenantId, BQRequestContextHolderUtils.getUserDetails().getTenantId());
        return getOne(queryWrapper);
    }
}
