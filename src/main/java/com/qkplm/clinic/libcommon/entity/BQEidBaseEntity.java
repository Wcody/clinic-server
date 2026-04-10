/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author: Wcke
 * @description: 系统业务类基类，主要与UUID字符串作为唯一标识
 * @datetime: 2024-06-12 15:31
 */
@Slf4j
@Data
public class BQEidBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 外部ID
     */
    @TableId(value = "eid", type = IdType.ASSIGN_UUID)
    private String eid;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    /**
     * 版本号
     */
    @Version
    private Integer version;
    /**
     * 逻辑删除标识
     */
    @TableLogic
    private Byte deleted;
    /**
     * 租户Id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    // 实现动态设置字段值的方法
    public void set(String fieldName, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName); // 根据字段名获取字段
            field.setAccessible(true); // 设置为可访问
            field.set(this, value); // 设置字段值
        } catch (Exception e) {
            //只记录错误
            log.error("Failed to set field: {}，错误信息：{}", fieldName, e.getMessage());
        }
    }

    // 实现动态获取字段值的方法
    public Object get(String fieldName) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName); // 根据字段名获取字段
            field.setAccessible(true);
            return field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get field: " + fieldName, e);
        }
    }

    public void assignFrom(BQEidBaseEntity source) {
        if (source == null)
            return;
        Class<?> clazz = source.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(source);
                if (Objects.nonNull(value)) {
                    this.set(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
    }
}
