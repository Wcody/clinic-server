/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl.security;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import com.qkplm.clinic.clinicserver.entity.TbTenantEntity;
import com.qkplm.clinic.clinicserver.entity.TbUserEntity;
import com.qkplm.clinic.clinicserver.service.ITbMenuService;
import com.qkplm.clinic.clinicserver.service.ITbTenantService;
import com.qkplm.clinic.clinicserver.service.ITbUserService;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.enums.BQPlatformEnum;
import com.qkplm.clinic.libcommon.security.BQSecurityPermissionUtils;
import com.qkplm.clinic.libcommon.security.BQSecurityUserDetails;
import com.qkplm.clinic.libcommon.utils.BQIDUtils;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-14 22:22
 */
@Slf4j
@Service
public class BQSecurityUserDetailsService implements UserDetailsService {
    private static final DateTimeFormatter TENANT_AUTH_EXPIRE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ITbUserService userService;
    private final ITbTenantService tenantService;
    private final ITbMenuService menuService;
    private final WebApplicationContext applicationContext;

    public BQSecurityUserDetailsService (ITbUserService userService, ITbTenantService tenantService, ITbMenuService menuService, WebApplicationContext applicationContext) {
        this.userService = userService;
        this.tenantService = tenantService;
        this.menuService = menuService;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void loadSystemPermissions() {
        long startTime = System.currentTimeMillis();
        try {
            // 缓存系统权限
            BQSecurityPermissionUtils.initAllPermissions(applicationContext);
        } finally {
            log.info("首次装载后，缓存系统权限，耗时：{}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * 首次装载后，缓存系统权限
     */
    @Override
    public UserDetails loadUserByUsername(String account) {
        boolean isSuper = BQSecurityUserDetails.getSuperAccount().equals(account);
        boolean isAdmin = BQSecurityUserDetails.getAdminAccount().equals(account);
        boolean isSuperAdmin = isSuper || isAdmin;
        // 获取登录时的租户ID
        String loginTenantId = BQRequestContextHolderUtils.getLoginTenantId();
        // 获取用户信息，查询数据库
        Optional<TbUserEntity> userOpt = StringUtils.isBlank(loginTenantId)
                ? userService.getByAccount(account)
                : userService.getByAccountAndTenantId(account, loginTenantId);
        if (userOpt.isEmpty()) {
            throw new BQApiException("用户名或密码错误");
        }
        String userTenantId = userOpt.get().getTenantId();
        boolean isPlatformTenantUser = StringUtils.equals(
                userTenantId,
                BQRequestContextHolderUtils.PLATFORM_TENANT_ID
        );
        // 平台租户用户固定使用平台租户，不参与诊所租户选择和存在性校验
        if (isPlatformTenantUser) {
            loginTenantId = userTenantId;
            log.info("平台租户用户登录：【{}】", loginTenantId);
        } else if (StringUtils.isBlank(loginTenantId)) {
            loginTenantId = userTenantId;
            log.info("登录租户ID：【{}】", loginTenantId);
            // 获取管理租户ID
            List<TbTenantEntity> tenants = tenantService.getTenantsBy(userOpt.get().getEid());
            if (!tenants.isEmpty()) {
                ArrayNode rows = buildLoginTenantOptions(tenants, userTenantId);
                if (rows.size() > 1) {
                    log.warn("用户【{}】拥有多个租户：【{}】", account, rows);
                    //返回登录页面选择租户后再进行登录
                    throw new BQApiException(10001, rows.toString());
                }
                loginTenantId = rows.get(0).path("value").asText(userTenantId);
            }
        }

        String loginTenantName = "";
        if (isPlatformTenantUser) {
            loginTenantId = userTenantId;
            loginTenantName = "平台";
        } else {
            TbTenantEntity loginTenant = tenantService.getByTenantId(loginTenantId);
            if (Objects.isNull(loginTenant)) {
                throw new BQApiException("诊所不存在");
            }
            assertTenantAuthorizationValid(loginTenant);
            loginTenantName = loginTenant.getName();
        }

        //判断是否为租户管理员
        boolean isTenantAdmin = tenantService.userIsTenantAdmin(userOpt.get().getEid(), loginTenantId);
        try {
            // 先设置登录租户ID，用于获取自己的角色权限
            BQRequestContextHolderUtils.setLoginTenantId(userOpt.get().getTenantId());
            //获取用户的所有角色ID，如果是租户管理员，则获取租户的
            List<String> roleIds = userService.getRoleIdsBy(userOpt.get().getEid());
            if (Objects.isNull(roleIds) || roleIds.isEmpty()) {
                if (isSuperAdmin || isTenantAdmin) {
                    roleIds = new ArrayList<>();
                    roleIds.add("111");
                } else {
                    throw new BQApiException("用户没有被分配任何角色");
                }
            }
            //根据角ID列表获取菜单ID列表
            List<String> menuIds = menuService.getMenuIdsBy(roleIds);
            if (isTenantAdmin) {
                // 直接获取管理租户的可用菜单ID列表
                menuIds = tenantService.getMenuIdsBy(loginTenantId);
            }
            if (Objects.isNull(menuIds) || menuIds.isEmpty()  && !isSuperAdmin) {
                if (isSuperAdmin) {
                    menuIds = new ArrayList<>();
                    menuIds.add("111");
                } else {
                    throw new BQApiException("用户没有被分配任何功能模块");
                }
            }
            // 租户管理员按诊所授权菜单取权限，普通用户按角色取权限
            List<String> permissions = isTenantAdmin
                    ? menuService.getAllButtonAuthsByMenuIds(menuIds)
                    : menuService.getAllButtonAuths(roleIds);
            if ((Objects.isNull(permissions) || permissions.isEmpty()) && !isSuperAdmin) {
                throw new BQApiException("用户没有任何访问权限");
            }
            List<String> extractPermissions = new ArrayList<>();
            if (isSuper) {
                extractPermissions.add("super");
            }
            if (isAdmin) {
                extractPermissions.add("admin");
            }
            //要将拼接在一起的分开
            for (String permission : permissions) {
                if (!StringUtils.isBlank(permission)) {
                    extractPermissions.addAll(List.of(permission.split(",")));
                }
            }

            // 返回SecurityUserDetails
            return BQSecurityUserDetails.builder()
                    .account(account)
                    .eid(userOpt.get().getEid())
                    .redisId(BQIDUtils.uuid())
                    .roles(roleIds)
                    .menus(menuIds)
                    .password(userOpt.get().getPassword())
                    .platform(BQPlatformEnum.PCB)
                    .permissions(extractPermissions)
                    .tenantId(loginTenantId)
                    .sourceTenantId(userOpt.get().getTenantId())
                    .nickname(userOpt.get().getNickname())
                    .email(userOpt.get().getEmail())
                    .phone(userOpt.get().getPhone())
                    .sex(userOpt.get().getSex())
                    .remark(userOpt.get().getRemark())
                    .avatar(userOpt.get().getAvatar())
                    .tenantName(loginTenantName)
                    .tenantAdmin(isTenantAdmin)
                    .ip(BQRequestContextHolderUtils.getRealRemoteIP())
                    .loginTime(LocalDateTime.now())
                    .build();
        } finally {
            BQRequestContextHolderUtils.removeLoginTenantId();
        }
    }

    /**
     * 临时授权和正式授权都必须受截止日期限制，永久授权不校验截止日期。
     */
    private void assertTenantAuthorizationValid(TbTenantEntity tenant) {
        if (Objects.equals(tenant.getAuthType(), 2)) {
            return;
        }
        LocalDateTime expireDate = tenant.getExpireDate();
        if (Objects.isNull(expireDate)) {
            throw new BQApiException("诊所授权未设置截止日期，不能登录");
        }
        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new BQApiException(String.format(
                    "诊所授权已过期，截止日期：%s",
                    expireDate.format(TENANT_AUTH_EXPIRE_FORMATTER)
            ));
        }
    }

    private ArrayNode buildLoginTenantOptions(List<TbTenantEntity> tenants, String userTenantId) {
        ArrayNode rows = BQJacksonUtils.newArrayNode();
        boolean bFound = false;
        for (TbTenantEntity tenant : tenants) {
            ObjectNode item = BQJacksonUtils.newObjectNode();
            item.put("value", tenant.getEid());
            item.put("label", tenant.getName());
            rows.add(item);

            if (tenant.getEid().equals(userTenantId)) {
                bFound = true;
            }
        }

        // 加入缺省租户
        if (!bFound) {
            TbTenantEntity tenant = tenantService.getByTenantId(userTenantId);
            ObjectNode item = BQJacksonUtils.newObjectNode();
            if (Objects.isNull(tenant)) {
                item.put("value", userTenantId);
                item.put("label", "缺省诊所");
            } else {
                item.put("value", tenant.getEid());
                item.put("label", tenant.getName());
            }
            rows.add(item);
        }
        return rows;
    }
}
