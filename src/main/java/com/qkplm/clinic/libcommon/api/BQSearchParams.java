/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * @author: Wcke
 * @description: 搜索条件，根据实际情况修改前后端字段匹配
 * @datetime: 2024-06-13 17:30
 */
@Slf4j
@Data
public class BQSearchParams<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public BQSearchParams() {
        size = -1L;
        page = 1L;
        orders = null;
        filters = null;
    }
    /**
     * 分页大小
     */
    private Long size;
    /**
     * 当前页
     */
    private Long page;
    /**
     * 排序字段列白哦
     */
    private List<OrderItem> orders;
    /**
     * 搜索条件列表
     */
    private List<BQSearchItem> filters;

    /**
     * 转换为mybatis-plus分页对象
     */
    public Page<T> toPage() {
        Page<T> pageObj = new Page<>(page, size);
        if (!Objects.isNull(orders)) {
            pageObj.addOrder(orders);
        }
        return pageObj;
    }

    private ArrayList<Object> getArray(Object object) {
        JsonNode jsonNode = BQJacksonUtils.jsonTo(String.valueOf(object));
        if (Objects.nonNull(jsonNode) && jsonNode.isArray() && !jsonNode.isEmpty()) {
            ArrayList<Object> list = new ArrayList<>();
            for (JsonNode node : jsonNode) {
                if (node.isTextual()) {
                    list.add(node.asText());
                } else {
                    list.add(node.asInt());
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * 转换为mybatis-plus查询条件
     */
    public QueryWrapper<T> toWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (filters != null) {
            for (BQSearchItem searchItem : filters) {
                switch (searchItem.getOperator()) {
                    case "likeLeft" ->
                        //左匹配
                            queryWrapper.likeLeft(searchItem.getField(), searchItem.getValue());
                    case "likeRight" ->
                        //右匹配
                            queryWrapper.likeRight(searchItem.getField(), searchItem.getValue());
                    case "like" ->
                        //模糊匹配
                            queryWrapper.like(searchItem.getField(), searchItem.getValue());
                    case "eq" ->
                        //等于
                            queryWrapper.eq(searchItem.getField(), searchItem.getValue());
                    case "ne" ->
                        //不等于
                            queryWrapper.ne(searchItem.getField(), searchItem.getValue());
                    case "gt" ->
                        //大于
                            queryWrapper.gt(searchItem.getField(), searchItem.getValue());
                    case "ge" ->
                        //大于等于
                            queryWrapper.ge(searchItem.getField(), searchItem.getValue());
                    case "lt" ->
                        //小于
                            queryWrapper.lt(searchItem.getField(), searchItem.getValue());
                    case "le" ->
                        //小于等于
                            queryWrapper.le(searchItem.getField(), searchItem.getValue());
                    case "in" ->
                        //在...中
                            queryWrapper.in(searchItem.getField(), getArray(searchItem.getValue()));
                    case "notIn" ->
                        //不在...中
                            queryWrapper.notIn(searchItem.getField(), searchItem.getValue());
                    case "isNull" ->
                        //为空
                            queryWrapper.isNull(searchItem.getField());
                    case "isNotNull" ->
                        //不为空
                            queryWrapper.isNotNull(searchItem.getField());
                    case "between" ->
                        //在...和...之间
                            queryWrapper.between(searchItem.getField(), searchItem.getValue(), searchItem.getValue2());
                    case "notBetween" ->
                        //不在...和...之间
                            queryWrapper.notBetween(searchItem.getField(), searchItem.getValue(), searchItem.getValue2());
                    case "orLike" -> {
                        // 多字段 OR 模糊匹配，field 用逗号分隔多个字段名
                        String[] fields = searchItem.getField().split(",");
                        final Object val = searchItem.getValue();
                        queryWrapper.and(w -> {
                            w.like(fields[0].trim(), val);
                            for (int i = 1; i < fields.length; i++) {
                                w.or().like(fields[i].trim(), val);
                            }
                        });
                    }
                    case null, default -> log.error("不支持的查询操作符：{}", searchItem.getOperator());
                }
            }
        }
        return queryWrapper;
    }

    /**
     * 设置排序字段
     */
    public void setOrders(String orders) {
        if (!Objects.isNull(orders)) {
            this.orders = BQJacksonUtils.jsonToBean(orders, new TypeReference<>() {
            });
        }
    }

    /**
     * 设置搜索条件
     */
    public void setFilters(String filters) {
        if (!Objects.isNull(filters)) {
            this.filters = BQJacksonUtils.jsonToBean(filters, new TypeReference<>() {
            });
        }
    }
}
