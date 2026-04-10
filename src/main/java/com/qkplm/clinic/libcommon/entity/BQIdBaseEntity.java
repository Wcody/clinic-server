/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: Wcke
 * @description: 系统日志类基类，主要与自增ID做为唯一标识，没有逻辑删除，没有乐观锁
 * @datetime: 2024-06-26 14:36
 */
@Data
public class BQIdBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 外部ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
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
     * 租户Id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;
}
