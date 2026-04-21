/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月22日
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
* @description <p>人员管理表 实体类</p>
* @datetime 2026-4-22 2:10
*/
@Getter
@Setter
@TableName(value = "bq_clinic_staff", autoResultMap = true)
public class BqClinicStaffEntity extends BQIdBaseEntity {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 所属科室
     */
    private String department;

    /**
     * 状态:启用/禁用
     */
    private String status;

    /**
     * [B]是否管理员:1是 0否
     */
    private Boolean isAdmin;

    /**
     * [B]是否可变更管理员:1是 0否
     */
    private Boolean changeAdmin;

    /**
     * 菜单权限(JSON格式)
     */
    private String menuPermissions;

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
     * [B]逻辑删除标记,0表示false,1表示true
     */
    private Boolean deleted;

    /**
     * 删除人名称,字符串,长度256
     */
    private String deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
