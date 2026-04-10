/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月10日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>病案字段别名表 实体类</p>
* @datetime 2026-4-10 18:51
*/
@Getter
@Setter
@TableName(value = "tb_field_alias", autoResultMap = true)
public class TbFieldAliasEntity extends BQEidBaseEntity {

    /**
     * 别名类型
     */
    private String aliasType;

    /**
     * 字段名
     */
    private String aliasValue;

    /**
     * 系统字段名
     */
    private String sysValue;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String remark;

    /**
     * 来源系统
     */
    private String sourceSystem;

    /**
     * 顺序值
     */
    private Integer seq;

    /**
     * [B]启用禁用
     */
    private Boolean status;
}
