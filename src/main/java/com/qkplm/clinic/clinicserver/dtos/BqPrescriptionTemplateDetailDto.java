/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月19日
*/
package com.qkplm.clinic.clinicserver.dtos;

import com.qkplm.clinic.clinicserver.entity.BqPrescriptionTemplateDetailEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Wcke
 * @description <p>处方模板明细DTO，在实体基础上附加药品单价</p>
 * @datetime 2026-4-19
 */
@Getter
@Setter
public class BqPrescriptionTemplateDetailDto extends BqPrescriptionTemplateDetailEntity {

    /**
     * 药品单价（关联 bq_drug.prescription_price 获取，不持久化到模板表）
     */
    private String price;
}
