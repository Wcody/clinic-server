/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.qkplm.clinic.clinicserver.entity.TbTenantEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;

import java.util.List;

/**
* @author Wcke
* @description <p>系统租户 映射器</p>
* @datetime 2024-6-23 7:56
*/
@Mapper
public interface TbTenantMapper extends IBaqiMapper<TbTenantEntity> {
    @Select("select userId from sys_tenant_user where tenantId = #{tenantId}")
    List<String> getUserIdsBy(String tenantId);

    @Select("select tenantId from sys_tenant_user where userId = #{userId}")
    List<String> getTenantIdsBy(String userId);

    @Select("SELECT b.* FROM sys_tenant_user a, sel_tenant b where a.tenantId = b.eid and a.userId = #{userId}")
    List<TbTenantEntity> getTenantsBy(String userId);

    @Delete("delete from sys_tenant_user where tenantId = #{tenantId}")
    void deleteUserIdsBy(String tenantId);

    @Insert("<script>" +
            "insert into sys_tenant_user(tenantId, userId) values " +
            "<foreach collection='userIds' item='userId' separator=','>" +
            "(#{tenantId}, #{userId})" +
            "</foreach>" +
            "</script>"
    )
    int saveUserIdsBy(String tenantId, Iterable<String> userIds);

    @Select("select menuId from sys_tenant_menu where tenantId = #{tenantId}")
    List<String> getMenuIdsBy(String tenantId);

    @Delete("delete from sys_tenant_menu where tenantId = #{tenantId}")
    void deleteMenuIdsBy(String tenantId);

    @Insert("<script>" +
            "insert into sys_tenant_menu(tenantId, menuId) values " +
            "<foreach collection='menuIds' item='menuId' separator=','>" +
            "(#{tenantId}, #{menuId})" +
            "</foreach>" +
            "</script>"
    )
    int saveMenuIdsBy(String tenantId, Iterable<String> menuIds);

    @Select("select * FROM sel_tenant where eid = #{tenantId}")
    TbTenantEntity selectByTenantId(String tenantId);

    @Select("select exists(select 1 FROM sys_tenant_user a, tb_tenant b where a.tenantId = b.eid and a.userId = #{userId} and b.eid=#{tenantId}) a")
    boolean userIsTenantAdmin(String userId, String tenantId);
}
