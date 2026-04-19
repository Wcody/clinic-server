/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月13日
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
* @description <p>收费明细表 实体类</p>
* @datetime 2026-4-13 23:9
*/
@Getter
@Setter
@TableName(value = "tb_fee_item", autoResultMap = true)
public class TbFeeItemEntity extends BQEidBaseEntity {

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * 收费单ID
     */
    private Integer feeId;

    /**
     * 1药品 2检查 3处置 4附加费
     */
    private Byte itemType;

    /**
     * 项目名称
     */
    private String itemName;

    private Integer num;

    private BigDecimal price;

    private BigDecimal totalPrice;

    private LocalDateTime createTime;
}
