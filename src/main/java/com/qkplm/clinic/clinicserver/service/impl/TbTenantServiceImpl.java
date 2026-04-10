/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.TbTenantEntity;
import com.qkplm.clinic.clinicserver.mapper.TbTenantMapper;
import com.qkplm.clinic.clinicserver.service.ITbTenantService;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;

import java.util.List;
import java.util.Objects;

/**
* @author Wcke
* @description <p>系统租户 服务接口类</p>
* @datetime 2024-6-16 15:49
*/
@Service
public class TbTenantServiceImpl extends BaqiServiceImpl<TbTenantMapper, TbTenantEntity> implements ITbTenantService {

    @Override
    public Object setStatus(String eid, Boolean status) {
        TbTenantEntity tenant = getById(eid);
        if (Objects.nonNull(tenant)) {
            tenant.setStatus(status);
            return updateById(tenant);
        }
        throw new BQApiException("目标不存在");
    }

    @Override
    public Object saveUserIds(String tenantId, List<String> userIds) {
        if (Objects.nonNull(userIds)) {
            getBaseMapper().deleteUserIdsBy(tenantId);
            return getBaseMapper().saveUserIdsBy(tenantId, userIds);
        }
        throw new BQApiException("无效参数");
    }

    @Override
    public List<String> getUserIdsBy(String tenantId) {
        return getBaseMapper().getUserIdsBy(tenantId);
    }

    @Override
    public List<String> getTenantIdsBy(String userId) {
        return getBaseMapper().getTenantIdsBy(userId);
    }

    @Override
    public List<TbTenantEntity> getTenantsBy(String userId) {
        return getBaseMapper().getTenantsBy(userId);
    }

    @Override
    public Object saveMenuIds(String tenantId, List<String> menuIds) {
        if (Objects.nonNull(menuIds)) {
            getBaseMapper().deleteMenuIdsBy(tenantId);
            return getBaseMapper().saveMenuIdsBy(tenantId, menuIds);
        };
        throw new BQApiException("无效参数");
    }

    @Override
    public List<String> getMenuIdsBy(String tenantId) {
        return getBaseMapper().getMenuIdsBy(tenantId);
    }

    @Override
    public TbTenantEntity getByTenantId(String tenantId) {
        return getBaseMapper().selectByTenantId(tenantId);
    }

    @Override
    public boolean userIsTenantAdmin(String userId, String tenantId) {
        return getBaseMapper().userIsTenantAdmin(userId, tenantId);
    }
}
