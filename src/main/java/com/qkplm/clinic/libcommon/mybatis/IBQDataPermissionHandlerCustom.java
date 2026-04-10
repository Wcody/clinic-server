/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.mybatis;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-29 11:16
 */
public interface IBQDataPermissionHandlerCustom {
    Expression handle(Table table, Expression where, String mappedStatementId);
}
