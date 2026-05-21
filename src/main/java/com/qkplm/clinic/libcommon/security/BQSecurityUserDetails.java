/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qkplm.clinic.libcommon.enums.BQPlatformEnum;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-14 22:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BQSecurityUserDetails implements UserDetails {
    private static final String SUPER_ACCOUNT = "super";
    private static final String ADMIN_ACCOUNT = "admin";
    /**
     * 登录账号
     */
    private String account;

    /**
     * 用户唯一ID
     */
    private String eid;

    /**
     * redis存储ID
     */
    private String redisId;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private Byte sex;

    /**
     * 电话
     */
    private String phone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 登录平台
     */
    @Default
    private BQPlatformEnum platform = BQPlatformEnum.PCB;

    /**
     * 账户登录的租户ID
     */
    private String tenantId;

    /**
     * 账户原来所属的租户ID
     */
    private String sourceTenantId;

    /**
     * 用户权限列表
     */
    private List<String> permissions;

    /**
     * 用户拥有角色ID列表
     */
    private List<String> roles;

    /**
     * 用户拥有菜单ID列表
     */
    private List<String> menus;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 诊所名
     */
    private String tenantName;

    /**
     * 诊所Logo
     */
    private String tenantLogo;

    /**
     * 账号是否过期
     */
    @Default
    private Boolean accountNonExpired = Boolean.TRUE;

    /**
     * 账号是否锁定
     */
    @Default
    private Boolean accountNonLocked = Boolean.TRUE;

    /**
     * 证书是否过期
     */
    @Default
    private Boolean credentialsNonExpired = Boolean.TRUE;

    /**
     * 账号是否可用
     */
    @Default
    private Boolean enabled = Boolean.TRUE;

    /**
     * 证书是否过期
     */
    @Default
    private Boolean tenantAdmin = Boolean.FALSE;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return account;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired == Boolean.TRUE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked == Boolean.TRUE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired == Boolean.TRUE;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled == Boolean.TRUE;
    }

    public boolean isSuperAccount() {
        return SUPER_ACCOUNT.equals(account);
    }

    public boolean isAdminAccount() {
        return ADMIN_ACCOUNT.equals(account);
    }

    public boolean isTenantAdmin() {
        return tenantAdmin == Boolean.TRUE;
    }

    public static String getSuperAccount() {
        return SUPER_ACCOUNT;
    }

    public static String getAdminAccount() {
        return ADMIN_ACCOUNT;
    }

    public ObjectNode toObjectNode() {
        return BQJacksonUtils.newObjectNode()
                .put("account", account)
                .put("nickname", nickname)
                .put("email", email)
                .put("sex", sex)
                .put("phone", phone)
                .put("remark", remark)
                .put("avatar", avatar)
                .put("platform", platform.name())
                .put("tenantId", tenantId)
                .put("ip", ip)
                .put("redisId", redisId)
                .put("eid", eid)
                .put("sourceTenantId", sourceTenantId)
                .put("tenantName", tenantName)
                .put("tenantLogo", tenantLogo)
                .putPOJO("loginTime", loginTime);
    }
}
