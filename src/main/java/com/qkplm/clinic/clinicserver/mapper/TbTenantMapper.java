/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    @Insert("insert into sys_tenant_menu(tenantId, menuId) select #{targetTenantId}, menuId from sys_tenant_menu where tenantId = #{sourceTenantId}")
    int copyMenuIdsBy(@Param("sourceTenantId") String sourceTenantId, @Param("targetTenantId") String targetTenantId);

    @Insert("insert into sys_role_menu(roleId, menuId) select #{targetRoleId}, menuId from sys_role_menu where roleId = #{sourceRoleId}")
    int copyRoleMenuIdsBy(@Param("sourceRoleId") String sourceRoleId, @Param("targetRoleId") String targetRoleId);

    @Insert("insert into sys_tenant_menu(tenantId, menuId) " +
            "select distinct #{targetTenantId}, a.menuId " +
            "from sys_role_menu a left join tb_role b on a.roleId = b.eid " +
            "where b.tenantId = #{targetTenantId}")
    int copyMenuIdsByTenantRoleIds(@Param("targetTenantId") String targetTenantId);

    @Select("select exists(select 1 FROM sys_tenant_user a, tb_tenant b where a.tenantId = b.eid and a.userId = #{userId} and b.eid=#{tenantId}) a")
    boolean userIsTenantAdmin(String userId, String tenantId);

    @Select("select count(*) from tb_user where tenantId = #{tenantId} and deleted = 0")
    int countUsersByTenantId(String tenantId);

    @Select("select count(*) from sys_tenant_user where tenantId = #{tenantId}")
    int countAdminsByTenantId(String tenantId);

    @Select("select count(*) from sys_tenant_menu where tenantId = #{tenantId}")
    int countMenusByTenantId(String tenantId);

    @Select("select eid from tb_role where tenantId = #{tenantId}")
    List<String> getRoleIdsByTenantId(String tenantId);

    @Delete("<script>" +
            "delete from sys_role_menu where roleId in " +
            "<foreach collection='roleIds' item='roleId' open='(' separator=',' close=')'>" +
            "#{roleId}" +
            "</foreach>" +
            "</script>"
    )
    int deleteRoleMenusByRoleIds(@Param("roleIds") Iterable<String> roleIds);

    @Delete("delete from tb_role where tenantId = #{tenantId}")
    int deleteRolesByTenantId(String tenantId);

    @Delete("delete from tb_dept where tenantId = #{tenantId}")
    int deleteDeptsByTenantId(String tenantId);

    @Delete("delete from bq_medical_dictionary where tenantId = #{tenantId}")
    int deleteMedicalDictionaryByTenantId(String tenantId);

    @Delete("delete from bq_enum_item where tenantId = #{tenantId}")
    int deleteEnumItemsByTenantId(String tenantId);

    @Delete("delete from bq_registration_fee where tenantId = #{tenantId}")
    int deleteRegistrationFeesByTenantId(String tenantId);

    @Delete("delete from bq_additional_fee where tenantId = #{tenantId}")
    int deleteAdditionalFeesByTenantId(String tenantId);

    @Delete("delete from bq_diagnosis_dict where tenantId = #{tenantId}")
    int deleteDiagnosisDictsByTenantId(String tenantId);

    @Delete("delete from bq_examine_item where tenantId = #{tenantId}")
    int deleteExamineItemsByTenantId(String tenantId);

    @Delete("delete from bq_treatment_item where tenantId = #{tenantId}")
    int deleteTreatmentItemsByTenantId(String tenantId);

    @Delete("delete from bq_clinic_department where tenantId = #{tenantId}")
    int deleteClinicDepartmentsByTenantId(String tenantId);

    @Delete("delete from bq_drug where tenantId = #{tenantId}")
    int deleteDrugsByTenantId(String tenantId);

    @Select("""
            select
              (select count(*) from bq_patient where tenantId = #{tenantId} and deleted = 0) +
              (select count(*) from bq_registration where tenantId = #{tenantId} and deleted = 0) +
              (select count(*) from bq_medical_record where tenantId = #{tenantId} and deleted = 0) +
              (select count(*) from bq_prescription where tenantId = #{tenantId} and deleted = 0)
            """)
    int countBusinessDataByTenantId(String tenantId);
}
