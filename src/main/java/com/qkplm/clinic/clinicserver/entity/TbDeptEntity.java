/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月11日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>部门 实体类</p>
* @datetime 2026-4-11 9:28
*/
@Getter
@Setter
@TableName(value = "tb_dept", autoResultMap = true)
public class TbDeptEntity extends BQEidBaseEntity {

    /**
     * 部门名称
     */
    private String name;

    /**
     * 上级部门ID
     */
    private String parentId;

    /**
     * 部门负责人
     */
    private String principal;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    private Integer orderValue;

    /**
     * [B]部门状态
     */
    private Boolean status;

    /**
     * 备注
     */
    private String remark;
}
