/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qkplm.clinic.clinicserver.constant.BqDataConst;
import com.qkplm.clinic.clinicserver.entity.BqParamDataEntity;
import com.qkplm.clinic.clinicserver.entity.BqParamItemEntity;
import com.qkplm.clinic.clinicserver.mapper.BqParamItemMapper;
import com.qkplm.clinic.clinicserver.service.IBqParamDataService;
import com.qkplm.clinic.clinicserver.service.IBqParamItemService;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.entity.BQTenantInfoEntity;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;

import java.util.Objects;

/**
* @author Wcke
* @description <p>系统参数项表 服务接口类</p>
* @datetime 2024-9-21 9:1
*/
@Service
public class BqParamItemServiceImpl extends BaqiServiceImpl<BqParamItemMapper, BqParamItemEntity> implements IBqParamItemService {
    private final IBqParamDataService paramDataService;

    public BqParamItemServiceImpl(IBqParamDataService paramDataService) {
        this.paramDataService = paramDataService;
    }

    private BqParamDataEntity getParamData(BqParamItemEntity paramItem) {
        String userId = "0";
        if (paramItem.getPid() == BqDataConst.MINE_PID) {
            userId = BQRequestContextHolderUtils.getUserDetails().getEid();
        }

        BqParamDataEntity paramData = paramDataService.getParamDataEntity(paramItem.getId(), userId);
        if (paramData == null) {
            paramData = new BqParamDataEntity();
            paramData.setParamId(paramItem.getId());
            paramData.setUserId(userId);
        }

        paramData.setParamValue(paramItem.getParamValue());
        return paramData;
    }

    @Override
    public BqParamItemEntity saveParamItem(BqParamItemEntity paramItem) {
        if (BQRequestContextHolderUtils.getUserDetails().isSuperAccount()) {
            if (!updateById(paramItem)) {
                throw new BQApiException("更新失败，对象可能已失效");
            }
        } else {
            if (paramItem.getPid() == BqDataConst.MINE_PID) BQRequestContextHolderUtils.setSourceTenantId();
            try {
                //个人参数，设置userId
                BqParamDataEntity paramData = getParamData(paramItem);
                if (!paramDataService.saveOrUpdate(paramData)) {
                    throw new BQApiException("更新失败，对象可能已失效");
                }
            } finally {
                if (paramItem.getPid() == BqDataConst.MINE_PID) BQRequestContextHolderUtils.removeSourceTenantId();
            }
        }
        return paramItem;
    }

    @Override
    public ObjectNode getRealParamData(Integer id) {
        if (Objects.nonNull(id)) {
            BqParamItemEntity paramItem = getById(id);
            if (paramItem != null) {
                if (BQRequestContextHolderUtils.getUserDetails().isSuperAccount()) {
                    return paramItem.getParamValue();
                }
                return paramDataService.getRealParamData(paramItem.getPid(), paramItem.getId(), paramItem.getParamValue());
            }
        }
        throw new BQApiException("参数项不存在");
    }

    @Override
    public BqParamItemEntity getRealParamItem(BqParamItemEntity defaultItem) {
        return paramDataService.getRealParamItem(defaultItem.getPid(), defaultItem.getId(), defaultItem);
    }

    @Override
    public BQTenantInfoEntity getTenantInfo() {
        BQTenantInfoEntity infoEntity = new BQTenantInfoEntity();
        ObjectNode nameNode = getRealParamData(BqDataConst.TENANT_NAME_ID);
        infoEntity.setTenantName(nameNode.get("value").asText());
        ObjectNode logoNode = getRealParamData(BqDataConst.TENANT_LOGO_ID);
        ArrayNode logoArray = (ArrayNode) logoNode.get("fileList");
        infoEntity.setTenantLogo(logoArray.get(0).asText());

        return infoEntity;
    }
}
