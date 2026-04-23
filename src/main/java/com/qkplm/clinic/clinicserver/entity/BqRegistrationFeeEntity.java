/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月23日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>挂号费设置表 实体类</p>
* @datetime 2026-4-23 11:50
*/
@Getter
@Setter
@TableName(value = "bq_registration_fee", autoResultMap = true)
public class BqRegistrationFeeEntity extends BQIdBaseEntity {

    /**
     * 诊所ID
     */
    private Long clinic;

    /**
     * 挂号类型名称
     */
    private String name;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * 售价
     */
    private BigDecimal sellingPrice;

    /**
     * [B]状态:1启用 0禁用
     */
    private Boolean status;

    /**
     * [B]是否常用:1是 0否
     */
    private Boolean common;

    /**
     * [B]是否默认:1是 0否
     */
    private Boolean isDefault;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 行乐观锁,整数,非空,默认0
     */
    private Integer version;

    /**
     * [B]逻辑删除标记,0表示false,1表示true
     */
    private Boolean deleted;

    /**
     * 删除人名称,字符串,长度256
     */
    private String deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
