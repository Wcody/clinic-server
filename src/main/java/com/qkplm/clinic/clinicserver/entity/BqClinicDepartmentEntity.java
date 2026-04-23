/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月23日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>科室管理表 实体类</p>
* @datetime 2026-4-23 11:50
*/
@Getter
@Setter
@TableName(value = "bq_clinic_department", autoResultMap = true)
public class BqClinicDepartmentEntity extends BQIdBaseEntity {

    /**
     * 诊所ID
     */
    private Long clinicId;

    /**
     * 诊所名称
     */
    private String clinicName;

    /**
     * 科室名称
     */
    private String name;

    /**
     * 科室人员(逗号分隔)
     */
    private String staff;

    /**
     * [B]是否常用:1是 0否
     */
    private Boolean common;

    /**
     * [B]是否删除:1是 0否
     */
    private Boolean deleted;

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
     * 删除人名称,字符串,长度256
     */
    private String deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
