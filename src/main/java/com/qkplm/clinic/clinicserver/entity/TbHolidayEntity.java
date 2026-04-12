/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月11日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>节假日配置表 实体类</p>
* @datetime 2026-4-11 9:28
*/
@Getter
@Setter
@TableName(value = "tb_holiday", autoResultMap = true)
public class TbHolidayEntity extends BQEidBaseEntity {

    /**
     * 名称
     */
    private String holidayName;

    /**
     * 日期
     */
    private LocalDate holidayDate;

    /**
     * [B]是否每年循环
     */
    private Boolean recurring;

    /**
     * [B]是否工作日
     */
    private Boolean workday;

    /**
     * [B]启用禁用
     */
    private Boolean status;

    /**
     * 节假日描述
     */
    private String remark;
}
