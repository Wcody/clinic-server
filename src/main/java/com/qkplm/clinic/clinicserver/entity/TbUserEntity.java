/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月29日
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
* @description <p>系统用户 实体类</p>
* @datetime 2026-4-29 17:45
*/
@Getter
@Setter
@TableName(value = "tb_user", autoResultMap = true)
public class TbUserEntity extends BQEidBaseEntity {

    /**
     * 所属部门ID
     */
    private String parentId;

    /**
     * 冗余的字段
     */
    private String parentName;

    /**
     * 登录账号
     */
    private String account;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 性别，0未知，1男，2女
     */
    private Byte sex;

    /**
     * [B]用户状态
     */
    private Boolean status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
    /**
     * [B]租户初始化数据
     */
    private Boolean tenantInitData;
}
