/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.extern.slf4j.Slf4j;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author: Wcke
 * @description: 租户处理器
 * @datetime: 2024-06-12 21:52
 */
@Slf4j
@Component
public class BQTenantHandler implements TenantLineHandler {
    /**
     * 不走租户路线的表
     */
    private static final List<String> IGNORE_TABLE_ARR = List.of(
            "tb_customer"
            , "tb_menu"
            , "bq_param_data"
            , "bq_param_group"
            , "bq_param_item"
    );

    @Override
    public Expression getTenantId() {
        String tenantId = BQRequestContextHolderUtils.getSourceTenantId();
        if (Objects.isNull(tenantId)) {
            tenantId = BQRequestContextHolderUtils.getUserDetails().getTenantId();
        }
        if (Objects.isNull(tenantId)) {
            log.error("诊所ID为空");
            throw new BQApiException(3001, "诊所ID为空");
        }
        return new StringValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return "tenantId";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 根据需要返回是否忽略该表
        return IGNORE_TABLE_ARR.contains(tableName) || tableName.startsWith("sys_") || tableName.startsWith("sel_");
    }
}
