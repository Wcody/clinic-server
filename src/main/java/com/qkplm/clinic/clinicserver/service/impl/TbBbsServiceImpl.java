/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.TbBbsEntity;
import com.qkplm.clinic.clinicserver.mapper.TbBbsMapper;
import com.qkplm.clinic.clinicserver.service.ITbBbsService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author Wcke
* @description <p>消息通知 服务接口类</p>
* @datetime 2025-4-8 11:38
*/
@Service
public class TbBbsServiceImpl extends BaqiServiceImpl<TbBbsMapper, TbBbsEntity> implements ITbBbsService {

    @Override
    public boolean addHandleBbs(String recordId, String content, int kind) {
        return false;
    }
}
