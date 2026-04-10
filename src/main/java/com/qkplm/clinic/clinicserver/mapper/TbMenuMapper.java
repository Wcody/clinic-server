/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
import com.qkplm.clinic.clinicserver.entity.TbMenuEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;

import java.util.List;

/**
* @author Wcke
* @description <p>系统菜单 映射器</p>
* @datetime 2024-6-22 19:56
*/
@Mapper
public interface TbMenuMapper extends IBaqiMapper<TbMenuEntity> {
    @Select("select menuId from sys_role_menu where roleId = #{roleId}")
    List<String> getMenuIdsBy(String roleId);

    @Select("<script>" +
            "select distinct a.menuId from sys_role_menu a left join tb_menu b on a.menuId = b.eid " +
            "where b.status = 1 and b.deleted = 0 and b.menuType &lt; 3 and a.roleId in " +
            "<foreach collection='roleIds' item='roleId' open='(' close=')' separator=','>" +
            "#{roleId}" +
            "</foreach>" +
            "</script>")
    List<String> getMenuIdsBys(List<String> roleIds);

    @Select("<script>" +
            "select distinct b.auths from sys_role_menu a left join tb_menu b on a.menuId = b.eid " +
            "where b.status = 1 and b.deleted = 0 and b.menuType = 3 and b.parentId = #{parentId} and a.roleId in " +
            "<foreach collection='roleIds' item='roleId' open='(' close=')' separator=','>" +
            "#{roleId}" +
            "</foreach>" +
            "</script>")
    List<String> getButtonAuthsBy(String parentId, List<String> roleIds);

    @Select("<script>" +
            "select distinct b.auths from sys_role_menu a left join tb_menu b on a.menuId = b.eid " +
            "where b.status = 1 and b.deleted = 0 and b.menuType = 3 and a.roleId in " +
            "<foreach collection='roleIds' item='roleId' open='(' close=')' separator=','>" +
            "#{roleId}" +
            "</foreach>" +
            "</script>")
    List<String> getAllButtonAuths(List<String> roleIds);

    @Delete("delete from sys_role_menu where roleId = #{roleId}")
    void deleteMenuIdsBy(String roleId);

    @Insert("<script>" +
            "insert into sys_role_menu(roleId, menuId) values " +
            "<foreach collection='menuIds' item='menuId' separator=','>" +
            "(#{roleId}, #{menuId})" +
            "</foreach>" +
            "</script>"
    )
    int saveMenuIdsBy(String roleId, Iterable<String> menuIds);
}
