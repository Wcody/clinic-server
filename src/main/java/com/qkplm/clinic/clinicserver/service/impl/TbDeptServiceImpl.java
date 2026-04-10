/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.TbDeptEntity;
import com.qkplm.clinic.clinicserver.entity.TbUserEntity;
import com.qkplm.clinic.clinicserver.mapper.TbDeptMapper;
import com.qkplm.clinic.clinicserver.service.ITbDeptService;
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
* @description <p>部门 服务接口类</p>
* @datetime 2024-6-21 17:6
*/
@Service
public class TbDeptServiceImpl extends BaqiServiceImpl<TbDeptMapper, TbDeptEntity> implements ITbDeptService {

    @Override
    public Object setStatus(String eid, Boolean status) {
        TbDeptEntity dept = getById(eid);
        if (Objects.nonNull(dept)) {
            dept.setStatus(status);
            return updateById(dept);
        }
        throw new BQApiException("目标不存在");
    }

    @Override
    public List<TbUserEntity> getDeptInfo(List<TbUserEntity> users) {
        if (Objects.isNull(users) || users.isEmpty()) {
            return users;
        }
        Set<String> deptIds = users.stream().map(TbUserEntity::getParentId).collect(Collectors.toSet());
        List<TbDeptEntity> deptList = listByIds(deptIds);
        Map<String, String> hashMap = deptList.stream().collect(toMap(TbDeptEntity::getEid, TbDeptEntity::getName));
        users.forEach(user -> {
            user.setParentName(hashMap.get(user.getParentId()));
        });
        return users;
    }
}
