/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月29日
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
* @description <p>附加费设置表 实体类</p>
* @datetime 2026-4-29 17:45
*/
@Getter
@Setter
@TableName(value = "bq_additional_fee", autoResultMap = true)
public class BqAdditionalFeeEntity extends BQIdBaseEntity {

    /**
     * 诊所ID
     */
    private Long clinic;

    /**
     * 附加费名称
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
     * [B]是否默认添加:1是 0否
     */
    private Boolean defaultAdd;

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

    /**
     * 名称拼音首字母
     */
    private String pinyin;
}
