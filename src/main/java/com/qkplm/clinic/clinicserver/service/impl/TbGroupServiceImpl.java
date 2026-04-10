/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.TbGroupEntity;
import com.qkplm.clinic.clinicserver.entity.TbTenantEntity;
import com.qkplm.clinic.clinicserver.mapper.TbGroupMapper;
import com.qkplm.clinic.clinicserver.service.ITbGroupService;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
* @author Wcke
* @description <p>租组 服务接口类</p>
* @datetime 2024-6-22 18:26
*/
@Service
public class TbGroupServiceImpl extends BaqiServiceImpl<TbGroupMapper, TbGroupEntity> implements ITbGroupService {

    @Override
    public Object setStatus(String eid, Boolean status) {
        TbGroupEntity group = getById(eid);
        if (Objects.nonNull(group)) {
            group.setStatus(status);
            return updateById(group);
        }
        throw new BQApiException("目标不存在");
    }

    @Override
    public List<TbTenantEntity> getGroupInfo(List<TbTenantEntity> tentantList) {
        if (Objects.isNull(tentantList) || tentantList.isEmpty()) {
            return tentantList;
        }
        Set<String> groupIds = tentantList.stream().map(TbTenantEntity::getParentId).collect(Collectors.toSet());
        List<TbGroupEntity> groupList = listByIds(groupIds);
        Map<String, String> hashMap = groupList.stream().collect(toMap(TbGroupEntity::getEid, TbGroupEntity::getName));
        tentantList.forEach(tenant -> {
            tenant.setParentName(hashMap.get(tenant.getParentId()));
        });
        return tentantList;
    }
}
