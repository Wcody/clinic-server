/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月22日
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
* @description <p>病案分类对照表 实体类</p>
* @datetime 2026-4-22 2:10
*/
@Getter
@Setter
@TableName(value = "tb_category_mapping", autoResultMap = true)
public class TbCategoryMappingEntity extends BQEidBaseEntity {

    /**
     * 对照名称
     */
    private String mappingName;

    /**
     * 第三方分类编码
     */
    private String otherCode;

    /**
     * 第三方分类名称
     */
    private String otherName;

    /**
     * 系统分类编码
     */
    private String sysCode;

    /**
     * 系统分类名称
     */
    private String sysName;

    /**
     * 来源系统
     */
    private String sourceSystem;

    /**
     * 描述
     */
    private String remark;

    /**
     * [B]启用禁用
     */
    private Boolean status;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
