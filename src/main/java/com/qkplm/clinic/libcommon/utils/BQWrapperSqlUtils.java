/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;

import java.util.Map;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2025-01-05 20:12
 */
public class BQWrapperSqlUtils {
    /**
     * 通过 Wrapper 构造完整的 SQL
     *
     * @param wrapper QueryWrapper 实例
     * @param entityClass 实体类类型
     * @return 构造的 SQL 语句
     */
    public static <T> String getSqlFromWrapper(QueryWrapper<T> wrapper, Class<T> entityClass) {
        // 获取表信息
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        if (tableInfo == null) {
            throw new IllegalArgumentException("实体类未正确映射到表，请检查 @TableName 配置。");
        }

        // 获取表名
        String tableName = tableInfo.getTableName();

        // 构造 SELECT 子句
        String selectClause = "*";

        // 构造基础 SQL
        StringBuilder sqlBuilder = new StringBuilder("SELECT ").append(selectClause).append(" FROM ").append(tableName);

        // 拼接 WHERE 条件
        String whereSegment = wrapper.getCustomSqlSegment();
        if (whereSegment != null && !whereSegment.trim().isEmpty()) {
            sqlBuilder.append(" WHERE ").append(whereSegment);
        }

        String originalSql = sqlBuilder.toString();
        // 提取参数映射
        Object paramObj = wrapper.getParamNameValuePairs();
        if (paramObj instanceof Map<?, ?> params) {
            for (Map.Entry<?, ?> entry : params.entrySet()) {
                // 拼接占位符完整格式
                String placeholder = "#{ew.paramNameValuePairs." + entry.getKey() + "}";
                // 获取参数值并处理字符串类型
                String value = entry.getValue() instanceof String
                        ? "'" + entry.getValue() + "'"  // 字符串加单引号
                        : entry.getValue().toString(); // 其他类型直接转为字符串
                // 替换占位符
                originalSql = originalSql.replace(placeholder, value);
            }
        }
        // 返回完整 SQL
        return originalSql;
    }
}
