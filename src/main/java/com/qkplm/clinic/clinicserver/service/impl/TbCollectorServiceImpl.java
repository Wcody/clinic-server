/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qkplm.clinic.clinicserver.entity.TbCollectorEntity;
import com.qkplm.clinic.clinicserver.mapper.TbCollectorMapper;
import com.qkplm.clinic.clinicserver.service.ITbCollectorService;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author Wcke
* @description <p>采集器 服务接口类</p>
* @datetime 2025-1-2 22:26
*/
@Service
public class TbCollectorServiceImpl extends BaqiServiceImpl<TbCollectorMapper, TbCollectorEntity> implements ITbCollectorService {

    @Override
    public Object setStatus(String eid, boolean status) {
        TbCollectorEntity collector = getById(eid);
        if (Objects.nonNull(collector)) {
            collector.setStatus(status);
            return updateById(collector);
        }
        throw new BQApiException("目标不存在");
    }

    @Override
    public TbCollectorEntity getByNameAndDeviceId(String collectorName, String deviceId) {
        if (Objects.nonNull(collectorName) && Objects.nonNull(deviceId)) {
            return getOne(new LambdaQueryWrapper<TbCollectorEntity>().eq(TbCollectorEntity::getCollectorName, collectorName).eq(TbCollectorEntity::getDeviceId, deviceId));
        }
        return null;
    }
}
