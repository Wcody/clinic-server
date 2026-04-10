/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.TbBbsEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

/**
* @author Wcke
* @description <p>消息通知 服务类</p>
* @datetime 2025-4-8 11:38
*/
public interface ITbBbsService extends IBaqiService<TbBbsEntity> {
    // 增加待审批通知
    boolean addHandleBbs(String recordId, String content, int kind);
}
