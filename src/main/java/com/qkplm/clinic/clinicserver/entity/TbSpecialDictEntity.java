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
* @description <p>特殊病历字典表 实体类</p>
* @datetime 2026-4-23 11:50
*/
@Getter
@Setter
@TableName(value = "tb_special_dict", autoResultMap = true)
public class TbSpecialDictEntity extends BQEidBaseEntity {

    /**
     * 对照名称
     */
    private String dictCode;

    /**
     * 第三方分类编码
     */
    private String dictName;

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
