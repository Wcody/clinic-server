/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月26日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>药品入库表 实体类</p>
* @datetime 2026-4-26 7:17
*/
@Getter
@Setter
@TableName(value = "bq_drug_warehousing", autoResultMap = true)
public class BqDrugWarehousingEntity extends BQIdBaseEntity {

    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 序号
     */
    private String seq;

    /**
     * 入库类型:采购入库/其他入库
     */
    private String drugWarehousingType;

    /**
     * 金额
     */
    private String amount;

    /**
     * 操作人
     */
    private String operatorPerson;

    /**
     * 创建日期
     */
    private LocalDate created;

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
