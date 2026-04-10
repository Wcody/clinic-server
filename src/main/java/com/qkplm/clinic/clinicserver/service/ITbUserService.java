/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.TbUserEntity;
import com.qkplm.clinic.clinicserver.dtos.BQUserKickOutDto;
import com.qkplm.clinic.clinicserver.dtos.BQUserOnlineListDto;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
* @author Wcke
* @description <p>系统用户 服务类</p>
* @datetime 2024-6-16 15:49
*/
public interface ITbUserService extends IBaqiService<TbUserEntity> {
    Optional<TbUserEntity> getByAccount(String account);

    Object resetPassword(List<String> infos);

    Object changePassword(List<String> passwords);

    Object saveRoleIds(String userId, List<String> roleIds);

    Object setStatus(String eid, Boolean status);

    List<String> getRoleIdsBy(String userId);

    Object onlineList(BQUserOnlineListDto params);

    Object kickOut(BQUserKickOutDto params);

    Boolean saveWithPassword(TbUserEntity user);

    Boolean saveBatchWithPassword(Collection<TbUserEntity> user);
}
