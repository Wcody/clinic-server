/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月13日
*/

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.BqPatientEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

/**
* @author Wcke
* @description <p>患者信息表 服务类</p>
* @datetime 2026-4-13 11:55
*/
public interface IBqPatientService extends IBaqiService<BqPatientEntity> {

    /**
     * 根据关键字查询患者列表（支持姓名、手机号、身份证号模糊查询）
     * @param keyword 关键字
     * @return 患者列表
     */
    Iterable<BqPatientEntity> searchByKeyword(String keyword);

}
