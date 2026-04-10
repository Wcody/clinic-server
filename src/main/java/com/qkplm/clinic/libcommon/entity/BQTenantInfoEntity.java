/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.entity;

import lombok.Data;

/**
 * @author: Wcke
 * @description: 租户信息类
 * @datetime: 2025-01-14 07:31
 */
@Data
public class BQTenantInfoEntity {
    private String tenantId;
    private String tenantName;
    private String tenantLogo;
}
