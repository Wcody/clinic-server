/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.TbRoleEntity;
import com.qkplm.clinic.clinicserver.mapper.TbRoleMapper;
import com.qkplm.clinic.clinicserver.service.ITbRoleService;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;

import java.util.Objects;

/**
* @author Wcke
* @description <p>系统角色 服务接口类</p>
* @datetime 2024-6-16 15:49
*/
@Service
public class TbRoleServiceImpl extends BaqiServiceImpl<TbRoleMapper, TbRoleEntity> implements ITbRoleService {

    @Override
    public Object setStatus(String eid, Boolean status) {
        TbRoleEntity role = getById(eid);
        if (Objects.nonNull(role)) {
            role.setStatus(status);
            return updateById(role);
        }
        throw new BQApiException("目标不存在");
    }
}
