/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月23日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>枚举头表 实体类</p>
* @datetime 2026-4-23 11:50
*/
@Getter
@Setter
@TableName(value = "tb_enum_header", autoResultMap = true)
public class TbEnumHeaderEntity extends BQEidBaseEntity {

    /**
     * 父ID
     */
    private String pid;

    /**
     * 枚举头名称
     */
    private String name;

    /**
     * [B]是否枚举分组
     */
    private Boolean grouped;

    /**
     * 说明
     */
    private String remark;

    /**
     * 顺序值
     */
    private Integer seq;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * 逻辑删除
     */
    private Integer delete;

    /**
     * [B]启用禁用
     */
    private Boolean status;
}
