/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月13日
*/
package com.qkplm.clinic.clinicserver.dtos;

import com.qkplm.clinic.clinicserver.entity.BqRegistrationEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Wcke
 * @description
 *              <p>
 *              挂号保存DTO（包含挂号信息和患者信息）
 *              </p>
 * @datetime 2026-4-13
 */
@Getter
@Setter
public class RegistrationSaveDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 挂号信息
     */
    private BqRegistrationEntity registration;
}
