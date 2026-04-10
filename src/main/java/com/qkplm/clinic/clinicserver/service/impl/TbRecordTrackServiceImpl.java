/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;
import com.qkplm.clinic.clinicserver.entity.TbRecordTrackEntity;
import com.qkplm.clinic.clinicserver.mapper.TbRecordTrackMapper;
import com.qkplm.clinic.clinicserver.service.ITbRecordTrackService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.security.BQSecurityUserDetails;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;

/**
* @author Wcke
* @description <p>病案追踪 服务接口类</p>
* @datetime 2024-12-17 7:29
*/
@Service
public class TbRecordTrackServiceImpl extends BaqiServiceImpl<TbRecordTrackMapper, TbRecordTrackEntity> implements ITbRecordTrackService {

    @Override
    public boolean addInsertTrackBy(TbRecordEntity recordEntity) {
        return addTrack(recordEntity.getEid(), "创建入库，等待护士质控");
    }

    @Override
    public boolean addUpdateTrackBy(TbRecordEntity recordEntity) {
        return addTrack(recordEntity.getEid(), "质控操作");
    }

    @Override
    public boolean addTrack(String recordId, String content) {
        BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
        TbRecordTrackEntity trackEntity = new TbRecordTrackEntity();
        trackEntity.setRecordId(recordId);
        trackEntity.setUserId(userDetails.getEid());
        trackEntity.setAccount(userDetails.getAccount());
        trackEntity.setContent(content);
        return save(trackEntity);
    }
}
