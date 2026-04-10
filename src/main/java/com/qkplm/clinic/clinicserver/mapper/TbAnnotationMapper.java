/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Select;
import com.qkplm.clinic.clinicserver.entity.TbAnnotationEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* @author Wcke
* @description <p>病案附件批注信息表 映射器</p>
* @datetime 2024-9-21 9:1
*/
@Mapper
public interface TbAnnotationMapper extends IBaqiMapper<TbAnnotationEntity> {
    /**
     * 获取批注扣除的总分
     */
    @Select("select sum(deductionPoints) from tb_annotation where recordId = #{recordId} and status = 1")
    Integer getTotalPoints(String recordId);

    /**
     * 获取每个附件的批注数量，行转列
     */
    @Select("select attachmentId, count(1) as count from tb_annotation where recordId = #{recordId} and status = 1 group by attachmentId")
    List<Map<String, Integer>> getCountByRecordId(String recordId);
}
