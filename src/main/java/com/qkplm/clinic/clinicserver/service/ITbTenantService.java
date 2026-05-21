/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qkplm.clinic.clinicserver.entity.TbTenantEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
* @author Wcke
* @description <p>系统租户 服务类</p>
* @datetime 2024-6-16 15:49
*/
public interface ITbTenantService extends IBaqiService<TbTenantEntity> {
    TbTenantEntity createTenant(TbTenantEntity tenant);

    Page<TbTenantEntity> pageWithUsage(Page<TbTenantEntity> page, Wrapper<TbTenantEntity> queryWrapper);

    Iterable<TbTenantEntity> listWithUsage(Page<TbTenantEntity> page, Wrapper<TbTenantEntity> queryWrapper);

    Object setStatus(String eid, Boolean status);

    Boolean deleteTenant(String tenantId);

    Boolean deleteTenantBatch(Collection<Serializable> tenantIds);

    Object saveUserIds(String tenantId, List<String> userIds);

    List<String> getUserIdsBy(String tenantId);

    List<String> getTenantIdsBy(String userId);

    List<TbTenantEntity> getTenantsBy(String userId);

    Object saveMenuIds(String tenantId, List<String> menuIds);

    List<String> getMenuIdsBy(String tenantId);

    TbTenantEntity getByTenantId(String tenantId);

    boolean userIsTenantAdmin(String userId, String tenantId);
}
