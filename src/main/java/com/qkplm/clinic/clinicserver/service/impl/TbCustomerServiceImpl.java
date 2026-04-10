/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.TbCustomerEntity;
import com.qkplm.clinic.clinicserver.mapper.TbCustomerMapper;
import com.qkplm.clinic.clinicserver.service.ITbCustomerService;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
* @author Wcke
* @description <p>使用系统的客户 服务接口类</p>
* @datetime 2024-7-23 10:28
*/
@Service
public class TbCustomerServiceImpl extends BaqiServiceImpl<TbCustomerMapper, TbCustomerEntity> implements ITbCustomerService {
    @Override
    public Object setStatus(String eid, Boolean status) {
        TbCustomerEntity customer = getById(eid);
        if (Objects.nonNull(customer)) {
            customer.setStatus(status);
            return updateById(customer);
        }
        throw new BQApiException("目标不存在");
    }

    @Override
    public Object saveUserIds(String customerId, List<String> userIds) {
        if (Objects.nonNull(userIds)) {
            getBaseMapper().deleteUserIdsBy(customerId);
            return getBaseMapper().saveUserIdsBy(customerId, userIds);
        }
        throw new BQApiException("无效参数");
    }

    @Override
    public List<String> getUserIdsBy(String customerId) {
        return getBaseMapper().getUserIdsBy(customerId);
    }

    @Override
    public List<String> getCustomerIdsBy(String userId) {
        return getBaseMapper().getCustomerIdsBy(userId);
    }

    @Override
    public Object saveMenuIds(String customerId, List<String> menuIds) {
        if (Objects.nonNull(menuIds)) {
            getBaseMapper().deleteMenuIdsBy(customerId);
            return getBaseMapper().saveMenuIdsBy(customerId, menuIds);
        };
        throw new BQApiException("无效参数");
    }

    @Override
    public List<String> getMenuIdsBy(String customerId) {
        return getBaseMapper().getMenuIdsBy(customerId);
    }
}
