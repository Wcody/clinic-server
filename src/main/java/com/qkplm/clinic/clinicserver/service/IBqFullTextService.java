/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;

/**
 * @author: Wcke
 * @description: 全文检索接口
 * @datetime: 2025-04-27 14:48
 */
public interface IBqFullTextService {
    /**
     * 病案全文索引
     */
    boolean saveRecord(TbRecordEntity entity);
}
