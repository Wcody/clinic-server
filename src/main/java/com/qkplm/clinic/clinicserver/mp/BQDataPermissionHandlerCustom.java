/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.mp;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.mybatis.IBQDataPermissionHandlerCustom;
import com.qkplm.clinic.libcommon.security.BQSecurityPermissionUtils;
import com.qkplm.clinic.libcommon.security.BQSecurityUserDetails;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;

import java.util.Objects;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-29 11:31
 */
public class BQDataPermissionHandlerCustom implements IBQDataPermissionHandlerCustom {
    public BQDataPermissionHandlerCustom () {
    }
    @Override
    public Expression handle(Table table, Expression where, String mappedStatementId) {
        try {
            BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
            if (!userDetails.isSuperAccount()) {
                BQAuthMark authMark = BQSecurityPermissionUtils.getAuthMark(BQRequestContextHolderUtils.getRequest().getRequestURI());
                if (Objects.nonNull(authMark) && authMark.needAuth()) {
                    String tableName = table.getName();
                    String aliasName = "";
                    Alias alias = table.getAlias();
                    if (Objects.nonNull(alias)) {
                        aliasName = String.format("%s.", alias.getName());
                    }
                    switch (tableName) {
                        case "tb_user" -> {
                            String sqlSegment = String.format("%seid <> '%s'", aliasName, "1817c651d3a54a9f9166ab090f704603");
                            return CCJSqlParserUtil.parseCondExpression(sqlSegment);
                        }
                        case "tb_role" -> {
                            String sqlSegment = String.format("%seid <> '%s'", aliasName, "9c24406ff699a00a24fdb9de38375dc9");
                            return CCJSqlParserUtil.parseCondExpression(sqlSegment);
                        }
                        case "tb_menu" -> {
                            String sqlSegment = String.format("(%1$seid <> '%2$s' AND %1$sparentId <> '%2$s')", aliasName, "95ef114fa5ca28b8edc2a80401dff1ac");
                            return CCJSqlParserUtil.parseCondExpression(sqlSegment);
                        }
                        case "bq_log" -> {
                            String sqlSegment = String.format("%saccount <> '%s'", aliasName, "super");
                            return CCJSqlParserUtil.parseCondExpression(sqlSegment);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new BQApiException(e.getMessage());
        }
        return null;
    }
}
