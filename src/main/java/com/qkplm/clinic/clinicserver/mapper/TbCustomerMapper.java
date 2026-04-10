/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import com.qkplm.clinic.clinicserver.entity.TbCustomerEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Wcke
* @description <p>使用系统的客户 映射器</p>
* @datetime 2024-7-23 10:28
*/
@Mapper
public interface TbCustomerMapper extends IBaqiMapper<TbCustomerEntity> {
    @Select("select userId from sys_customer_user where customerId = #{customerId}")
    List<String> getUserIdsBy(String customerId);

    @Select("select customerId from sys_customer_user where userId = #{userId}")
    List<String> getCustomerIdsBy(String userId);

    @Delete("delete from sys_customer_user where customerId = #{customerId}")
    void deleteUserIdsBy(String customerId);

    @Insert("<script>" +
            "insert into sys_customer_user(customerId, userId) values " +
            "<foreach collection='userIds' item='userId' separator=','>" +
            "(#{customerId}, #{userId})" +
            "</foreach>" +
            "</script>"
    )
    int saveUserIdsBy(String customerId, Iterable<String> userIds);

    @Select("select menuId from sys_customer_menu where customerId = #{customerId}")
    List<String> getMenuIdsBy(String customerId);

    @Delete("delete from sys_customer_menu where customerId = #{customerId}")
    void deleteMenuIdsBy(String customerId);

    @Insert("<script>" +
            "insert into sys_customer_menu(customerId, menuId) values " +
            "<foreach collection='menuIds' item='menuId' separator=','>" +
            "(#{customerId}, #{menuId})" +
            "</foreach>" +
            "</script>"
    )
    int saveMenuIdsBy(String customerId, Iterable<String> menuIds);
}
