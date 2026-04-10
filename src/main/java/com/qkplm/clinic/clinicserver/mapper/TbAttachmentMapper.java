/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Select;
import com.qkplm.clinic.clinicserver.entity.TbAttachmentEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Wcke
* @description <p>病案附件关联表 映射器</p>
* @datetime 2024-9-21 9:1
*/
@Mapper
public interface TbAttachmentMapper extends IBaqiMapper<TbAttachmentEntity> {
    @Select("SELECT kindId FROM tb_attachment where recordId = #{recordId} group by kindId")
    List<String> getKindIdsByRecordId(String recordId);
}
