/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.controller;

import com.qkplm.clinic.libcommon.utils.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.qkplm.clinic.clinicserver.entity.TbUserEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQApiResult;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;
import com.qkplm.clinic.libcommon.constant.BQRedisKeyConstant;
import com.qkplm.clinic.libcommon.entity.BQAuthInfoEntity;
import com.qkplm.clinic.libcommon.entity.BQLoginInfo;
import com.qkplm.clinic.libcommon.enums.BQLogTypeEnum;
import com.qkplm.clinic.libcommon.security.BQSecurityUserDetails;
import com.qkplm.clinic.libcommon.utils.*;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-15 11:41
 */
@Slf4j
@RestController
@RequestMapping("/ams/api/v1/user")
public class BQLoginController {
    private final static String MODULE_NAME = "用户管理";
    private final static String TAG_NAME = "user";
    @Resource
    private AuthenticationManager authenticationManager;

    @Value("${jwt.expirationTime}")
    private int expirationTime;

    @BQAuthMark(tag = TAG_NAME, needAuth = false)
    @BQLogMark(module = MODULE_NAME, operation = "登录", loginType = BQLogTypeEnum.LOGIN)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestBody TbUserEntity user) {
        // 设置登录租户ID
        BQRequestContextHolderUtils.setLoginTenantId(user.getTenantId());
        try {
            long start = System.currentTimeMillis();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getAccount(), user.getPassword());
            log.info("1耗时：{}ms", System.currentTimeMillis() - start);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("2耗时：{}ms", System.currentTimeMillis() - start);
            // 判断是否验证成功
            if (Objects.isNull(authentication)) {
                throw new BQApiException("用户名或密码错误");
            }
            log.info("3耗时：{}ms", System.currentTimeMillis() - start);
            //获取用户数据
            BQSecurityUserDetails userDetails = (BQSecurityUserDetails) authentication.getPrincipal();
            log.info("4耗时：{}ms", System.currentTimeMillis() - start);
            //拼接平台标识和ID
            String platform = String.format("%s:%s", userDetails.getPlatform().getValue(), userDetails.getRedisId());
            log.info("5耗时：{}ms", System.currentTimeMillis() - start);
            //根据用户名和数据标识生成token
            String token = BQJwtUtils.generateToken(user.getAccount(), platform);
            log.info("6耗时：{}ms", System.currentTimeMillis() - start);
            //拼接key
            String key = BQRedisKeyConstant.onlineUserKey(user.getAccount(), platform);
            log.info("7耗时：{}ms", System.currentTimeMillis() - start);
            //缓存用户数据
            BQRedisUtils.set(key, userDetails, expirationTime, TimeUnit.SECONDS);
            log.info("8耗时：{}ms", System.currentTimeMillis() - start);

            //登录成功，写入上下文
            BQRequestContextHolderUtils.setUserDetails(userDetails);

            //将日期格式化成yyyy/MM/dd hh:mm:ss
            Date date = DateUtils.addSeconds(new Date(), expirationTime);

            //返回加密的token
            return BQApiResult.ok(BQLoginInfo.builder()
                    .eid(userDetails.getEid())
                    .accessToken(token)
                    .expires(BQDateUtils.format("yyyy/MM/dd HH:mm:ss", date))
                    .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .account(userDetails.getUsername())
                    .nickname(userDetails.getNickname())
                    .email(userDetails.getEmail())
                    .phone(userDetails.getPhone())
                    .sex(userDetails.getSex())
                    .remark(userDetails.getRemark())
                    .lid(userDetails.getRedisId())
                    .avatar(userDetails.getAvatar())
                    .tenantLogo(userDetails.getTenantLogo())
                    .tenantName(userDetails.getTenantName())
                    .tenantId(userDetails.getTenantId())
                    .build()
            );
        } finally {
            BQRequestContextHolderUtils.removeLoginTenantId();
        }
    }

    @BQAuthMark(tag = TAG_NAME, needGrant = false)
    @BQLogMark(module = MODULE_NAME, operation = "注销", loginType = BQLogTypeEnum.LOGOFF)
    @RequestMapping(value = "/logoff", method = RequestMethod.POST)
    public Object logoff(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.isNull(authentication)) {
            BQSecurityUserDetails userDetails = (BQSecurityUserDetails) authentication.getPrincipal();
            //拼接平台标识和ID
            String platform = String.format("%s:%s", userDetails.getPlatform().getValue(), userDetails.getRedisId());
            //拼接key
            String key = BQRedisKeyConstant.onlineUserKey(userDetails.getAccount(), platform);
            //删除缓存
            BQRedisUtils.delete(key);
        }
        return BQApiResult.ok("退出成功");
    }

    @BQAuthMark(tag = TAG_NAME, needGrant = false)
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Object info(BQSearchParamsGet<TbUserEntity> paramsGet) {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @BQAuthMark(tag = TAG_NAME, needGrant = false)
    @RequestMapping(value = "/reg", method = RequestMethod.GET)
    public Object reg(BQSearchParamsGet<TbUserEntity> paramsGet) {
        return BQRequestContextHolderUtils.getUserDetails();
    }

    @BQAuthMark(tag = TAG_NAME, needGrant = false)
    @BQLogMark(module = MODULE_NAME, operation = "刷新登录")
    @RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
    public Object refreshToken(String token) {
        log.info("需要刷新token：{}", token);
        BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
        throw new BQApiException(1001, "Token已过期，请重新登录");
    }

    @BQAuthMark(tag = TAG_NAME, needAuth = false)
    @BQLogMark(module = MODULE_NAME, operation = "获取系统信息")
    @RequestMapping(value = "/sysInfo", method = RequestMethod.POST)
    public Object sysInfo() {
        BQAuthInfoEntity sysAuthEntity = BQAuthUtils.getSysAuthEntity();
        if (Objects.isNull(sysAuthEntity)) {
            sysAuthEntity = BQAuthInfoEntity.builder().deviceCode(BQDeviceUtils.getDeviceCode()).build();
        } else {
            sysAuthEntity.setDeviceCode(BQDeviceUtils.getDeviceCode());
        }
        return sysAuthEntity;
    }
}
