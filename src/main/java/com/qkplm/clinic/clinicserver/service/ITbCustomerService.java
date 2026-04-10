/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.TbCustomerEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.util.List;

/**
* @author Wcke
* @description <p>使用系统的客户 服务类</p>
* @datetime 2024-7-23 10:28
*/
public interface ITbCustomerService extends IBaqiService<TbCustomerEntity> {
    Object setStatus(String eid, Boolean status);

    Object saveUserIds(String customerId, List<String> userIds);

    List<String> getUserIdsBy(String customerId);

    List<String> getCustomerIdsBy(String userId);

    Object saveMenuIds(String customerId, List<String> menuIds);

    List<String> getMenuIdsBy(String customerId);
}
