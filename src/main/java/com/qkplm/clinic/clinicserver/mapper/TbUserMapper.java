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
import com.qkplm.clinic.clinicserver.entity.TbUserEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;

import java.util.List;

/**
* @author Wcke
* @description <p>系统用户 映射器</p>
* @datetime 2024-6-23 7:56
*/
@Mapper
public interface TbUserMapper extends IBaqiMapper<TbUserEntity> {
    @Select("select a.roleId from sys_user_role a left join tb_role b on a.roleId = b.eid where a.userId = #{userId} and b.status = 1 and b.deleted = 0")
    List<String> getRoleIdsBy(String userId);

    @Delete("delete from sys_user_role where userId = #{userId}")
    void deleteRoleIdsBy(String userId);

    @Insert("<script>" +
            "insert into sys_user_role(userId, roleId) values " +
            "<foreach collection='roleIds' item='roleId' separator=','>" +
            "(#{userId}, #{roleId})" +
            "</foreach>" +
            "</script>"
    )
    int saveRoleIdsBy(String userId, Iterable<String> roleIds);

    @Select("select * from sel_user where account = #{account}")
    TbUserEntity selectByAccount(String account);
}
