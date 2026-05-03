/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月18日
*/

package com.qkplm.clinic.clinicserver.mapper;

import com.qkplm.clinic.clinicserver.dtos.BqPrescriptionPrintVo;
import com.qkplm.clinic.clinicserver.entity.BqPrescriptionEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Wcke
 * @description
 *              <p>
 *              处方主表 映射器
 *              </p>
 * @datetime 2026-4-18 0:54
 */
@Mapper
public interface BqPrescriptionMapper extends IBaqiMapper<BqPrescriptionEntity> {

    @Select("SELECT ifnull(max(prescNo),0) + 1 FROM bq_prescription where createdTime between #{startDate} and #{endDate}")
    Long getNextPrescNo(String startDate, String endDate);

    /**
     * 打印处方所需 JOIN 查询：一次性取处方、挂号、患者、病历、频次/用法枚举
     */
    @Select("""
            SELECT
                p.id,
                p.prescNo,
                p.prescType,
                p.totalPrice,
                p.doseAmount,
                p.recommendation,
                p.usageType,
                p.frequence,
                p.days,
                p.regId,
                r.patient       AS patientName,
                r.gender,
                r.firstAge,
                r.lastAge,
                r.ageType,
                r.orderTime,
                r.doctor,
                r.registrationNo,
                pt.idCard,
                pt.mobile       AS phone,
                CONCAT(IFNULL(sp.name,''), IFNULL(sc.name,''), IFNULL(sd.name,''), IFNULL(pt.address,'')) AS address,
                pt.allergicHistory,
                pt.archiveNo,
                mr.diagnosis,
                mr.treatAdvice  AS treatmentSuggest,
                md_u.name       AS usageTypeName,
                md_f.name       AS frequenceName
            FROM bq_prescription p
            LEFT JOIN bq_registration       r    ON r.id       = p.regId
            LEFT JOIN bq_patient            pt   ON pt.id      = p.patientId
            LEFT JOIN bq_medical_record     mr   ON mr.id      = p.recordId
            LEFT JOIN bq_medical_dictionary md_u ON md_u.id    = p.usageType
            LEFT JOIN bq_medical_dictionary md_f ON md_f.id    = p.frequence
            LEFT JOIN sys_province          sp   ON sp.id      = pt.province
            LEFT JOIN sys_city              sc   ON sc.id      = pt.city
            LEFT JOIN sys_district          sd   ON sd.id      = pt.district
            WHERE p.regId = #{regId}
              AND p.deleted = 0
            ORDER BY p.id ASC
            """)
    List<BqPrescriptionPrintVo> queryForPrint(@Param("regId") Integer regId);
}
