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
* @description <p>档案分类 实体类</p>
* @datetime 2026-4-10 18:51
*/
@Getter
@Setter
@TableName(value = "tb_kind", autoResultMap = true)
public class TbKindEntity extends BQEidBaseEntity {

    /**
     * 档案分类标识
     */
    private String code;

    /**
     * 档案分类名称
     */
    private String name;

    /**
     * [B]启用禁用状态
     */
    private Boolean status;

    /**
     * 0档案、1临床、2门急诊
     */
    private Integer kind;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 资料中分类的顺序
     */
    private Integer orderValue;
}
