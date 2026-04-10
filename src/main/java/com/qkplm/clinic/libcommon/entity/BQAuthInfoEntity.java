/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Wcke
 * @description: 系统授权信息实体类
 * @datetime: 2024-10-26 20:22
 */
@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BQAuthInfoEntity {
    private String clientId;
    private String clientName;
    private Integer authType;
    private String expireDate;
    private String deviceCode;
    private String regCode;
    private Integer maxTenantCount;
    private Integer maxUserCount;
}
