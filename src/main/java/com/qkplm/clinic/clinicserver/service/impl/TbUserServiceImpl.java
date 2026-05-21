/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.qkplm.clinic.clinicserver.entity.TbUserEntity;
import com.qkplm.clinic.clinicserver.mapper.TbUserMapper;
import com.qkplm.clinic.clinicserver.service.ITbUserService;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.clinicserver.dtos.BQUserKickOutDto;
import com.qkplm.clinic.clinicserver.dtos.BQUserOnlineListDto;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.constant.BQRedisKeyConstant;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import com.qkplm.clinic.libcommon.security.BQSecurityUserDetails;
import com.qkplm.clinic.libcommon.utils.BQRedisUtils;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;

import java.util.*;

/**
* @author Wcke
* @description <p>系统用户 服务接口类</p>
* @datetime 2024-6-16 15:49
*/
@Service
public class TbUserServiceImpl extends BaqiServiceImpl<TbUserMapper, TbUserEntity> implements ITbUserService {
    private final PasswordEncoder passwordEncoder;

    public TbUserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<TbUserEntity> getByAccount(String account) {
//        LambdaQueryWrapper<TbUserEntity> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(TbUserEntity::getAccount, account);
//        return Optional.ofNullable(getOne(queryWrapper));
        return Optional.ofNullable(getBaseMapper().selectByAccount(account));
    }

    @Override
    public Optional<TbUserEntity> getByAccountAndTenantId(String account, String tenantId) {
        return Optional.ofNullable(getBaseMapper().selectByAccountAndTenantId(account, tenantId));
    }

    @Override
    public Object resetPassword(List<String> infos) {
        if (Objects.nonNull(infos) && infos.size() > 1) {
            String userId = infos.get(0);
            String password = infos.get(1);
            if (Objects.nonNull(userId) && Objects.nonNull(password)) {
                TbUserEntity user = getById(userId);
                if (Objects.nonNull(user)) {
                    password = passwordEncoder.encode(password);
                    user.setPassword(password);
                    return updateById(user);
                }
            }
        }
        throw new BQApiException("无效参数");
    }

    @Override
    public Object changePassword(List<String> passwords) {
        BQRequestContextHolderUtils.setSourceTenantId();
        try {
            if (Objects.nonNull(passwords) && passwords.size() > 1) {
                String oldPassword = passwords.get(0);
                String newPassword = passwords.get(1);
                if (Objects.nonNull(oldPassword) && Objects.nonNull(newPassword)) {
                    BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
                    String userId = userDetails.getEid();
                    TbUserEntity user = getById(userId);
                    if (Objects.nonNull(user)) {
                        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                            newPassword = passwordEncoder.encode(newPassword);
                            user.setPassword(newPassword);
                            return updateById(user);
                        } else {
                            throw new BQApiException("旧密码错误");
                        }

                    }
                }
            }
            throw new BQApiException("无效参数");
        } finally {
            BQRequestContextHolderUtils.removeSourceTenantId();
        }
    }

    @Override
    public Object saveRoleIds(String userId, List<String> roleIds) {
        if (Objects.nonNull(roleIds)) {
            getBaseMapper().deleteRoleIdsBy(userId);
            return getBaseMapper().saveRoleIdsBy(userId, roleIds);
        }
        throw new BQApiException("无效参数");
    }

    @Override
    public Object setStatus(String eid, Boolean status) {
        TbUserEntity user = getById(eid);
        if (Objects.nonNull(user)) {
            user.setStatus(status);
            return updateById(user);
        }
        throw new BQApiException("目标不存在");
    }

    @Override
    public List<String> getRoleIdsBy(String userId) {
        return getBaseMapper().getRoleIdsBy(userId);
    }

    @Override
    public Object onlineList(BQUserOnlineListDto params) {
        BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
        Collection<String> keys = BQRedisUtils.keys(BQRedisKeyConstant.onlineUsersAllKey());
        List<ObjectNode> users = new ArrayList<>();
        for (String key : keys) {
            BQSecurityUserDetails user = BQRedisUtils.get(key);
            if (user.isSuperAccount() && !userDetails.isSuperAccount()) {
                // 如果不是超级管理员，不返回超级管理员的登录信息
                continue;
            }
            users.add(user.toObjectNode());
        }
        return users;
    }

    @Override
    public Object kickOut(BQUserKickOutDto params) {
        String platform = String.format("%s:%s", params.getPlatform(), params.getRedisId());
        //根据用户名和数据标识生成token
        String key = BQRedisKeyConstant.onlineUserKey(params.getAccount(), platform);
        BQSecurityUserDetails userDetails = BQRedisUtils.get(key);
        if (Objects.isNull(userDetails)) {
            throw new BQApiException("用户已下线");
        }
        return BQRedisUtils.delete(key);
    }

    @Override
    public Boolean saveWithPassword(TbUserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return save(user);
    }

    @Override
    public Boolean saveBatchWithPassword(Collection<TbUserEntity> users) {
        users.forEach(item -> item.setPassword(passwordEncoder.encode(item.getPassword())));
        return saveBatch(users);
    }
}
