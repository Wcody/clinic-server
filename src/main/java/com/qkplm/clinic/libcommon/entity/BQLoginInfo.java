/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-18 17:59
 */
@Data
@Builder
public class BQLoginInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
//    /** token */
//    accessToken: string;
//    /** `accessToken`的过期时间（时间戳） */
//    expires: T;
//    /** 用于调用刷新accessToken的接口时所需的token */
//    refreshToken: string;
//    /** 头像 */
//    avatar?: string;
//    /** 用户名 */
//    account?: string;
//    /** 昵称 */
//    nickname?: string;
//    /** 当前登录用户的角色 */
//    roles?: Array<string>;
    /** token */
    private String accessToken;
    /** 用于调用刷新accessToken的接口时所需的token */
    private String refreshToken;
    /** `accessToken`的过期时间（时间戳） */
    private String expires;
    /** 用户id */
    private String eid;
    /** 登录id */
    private String lid;
    /** 头像 */
    private String avatar;
    /** 用户名 */
    private String account;
    /** 昵称 */
    private String nickname;
    /** 邮箱 */
    private String email;
    /** 电话 */
    private String phone;
    /** 性别 */
    private Byte sex;
    /** 备注 */
    private String remark;
    /** 当前登录用户的角色 */
    private List<String> roles;
    /** 诊所logo */
    private String tenantLogo;
    /** 诊所名称 */
    private String tenantName;
}
