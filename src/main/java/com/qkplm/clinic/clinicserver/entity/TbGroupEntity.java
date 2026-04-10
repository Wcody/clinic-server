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
* @description <p>客户组 实体类</p>
* @datetime 2026-4-10 18:51
*/
@Getter
@Setter
@TableName(value = "tb_group", autoResultMap = true)
public class TbGroupEntity extends BQEidBaseEntity {

    /**
     * 0租组，1客户组
     */
    private Integer kindId;

    /**
     * 客户组名称
     */
    private String name;

    /**
     * 上级客户组ID
     */
    private String parentId;

    /**
     * 客户组负责人
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
     * [B]启用禁用
     */
    private Boolean status;

    /**
     * 备注
     */
    private String remark;
}
