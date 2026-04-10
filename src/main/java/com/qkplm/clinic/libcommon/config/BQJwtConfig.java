/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.config;

import jakarta.annotation.PostConstruct;
import com.qkplm.clinic.libcommon.utils.BQJwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-14 23:15
 */
@Slf4j
@Component
public class BQJwtConfig {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expirationTime}")
    private long expirationTime;
    @Value("${jwt.header}")
    private String header;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @PostConstruct
    public void jwtInit() {
        BQJwtUtils.initialize(header, tokenHead, issuer, secretKey, expirationTime);
        log.info("BQJwtUtils初始化完成");
    }
}
