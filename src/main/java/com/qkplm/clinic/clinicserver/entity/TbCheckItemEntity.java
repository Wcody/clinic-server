/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月23日
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
* @description <p>检查检验项目表 实体类</p>
* @datetime 2026-4-23 11:50
*/
@Getter
@Setter
@TableName(value = "tb_check_item", autoResultMap = true)
public class TbCheckItemEntity extends BQEidBaseEntity {

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    private String itemCode;

    /**
     * 检查项目名称
     */
    private String itemName;

    private BigDecimal price;

    private Byte status;
}
