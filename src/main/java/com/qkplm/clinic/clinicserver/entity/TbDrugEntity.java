/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月11日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>药品字典表 实体类</p>
* @datetime 2026-4-11 9:28
*/
@Getter
@Setter
@TableName(value = "tb_drug", autoResultMap = true)
public class TbDrugEntity extends BQEidBaseEntity {

    private String drugCode;

    private String drugName;

    private String spec;

    private String unit;

    /**
     * 1西药 2中成药 3中药
     */
    private Byte drugType;

    private BigDecimal salePrice;

    private Byte status;

    private LocalDateTime createTime;
}
