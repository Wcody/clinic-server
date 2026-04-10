/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.qkplm.clinic.libcommon.security.BQSecurityUserDetails;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author: Wcke
 * @description: 填充公共字段
 * @datetime: 2024-06-12 18:30
 */
@Slf4j
@Component
public class BQMyBatisPlusMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充...");
        BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updatedTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "createdBy", String.class, userDetails.getAccount());
        this.strictInsertFill(metaObject, "updatedBy", String.class, userDetails.getAccount());
        this.strictInsertFill(metaObject, "tenantId", String.class, userDetails.getTenantId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充...");
        BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
        this.strictUpdateFill(metaObject, "updatedTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updatedBy", String.class, userDetails.getAccount());
    }
}
