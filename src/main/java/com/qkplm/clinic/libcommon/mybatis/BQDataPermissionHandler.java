/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;

import java.util.Objects;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-12 23:37
 */
@Slf4j
public class BQDataPermissionHandler implements MultiDataPermissionHandler {
    // 自定义数据权限处理器
    private static IBQDataPermissionHandlerCustom handlerCustom;

    /**
     * 初始化自定义数据权限处理器
     */
    public static void initHandlerCustom(IBQDataPermissionHandlerCustom handlerCustom) {
        if (Objects.isNull(BQDataPermissionHandler.handlerCustom) && Objects.nonNull(handlerCustom)) {
            BQDataPermissionHandler.handlerCustom = handlerCustom;
        }
    }
    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        // 在此处编写自定义数据权限逻辑
        try {
            if (Objects.nonNull(handlerCustom)) {
                return handlerCustom.handle(table, where, mappedStatementId);
            } else {
                // 数据权限相关的SQL片段
                //String sqlSegment = "name = 'admin'";
                String sqlSegment = "";
                return CCJSqlParserUtil.parseCondExpression(sqlSegment);
            }
        } catch (JSQLParserException e) {
            log.error("数据权限SQL片段解析失败", e);
            return null;
        }
    }
}
