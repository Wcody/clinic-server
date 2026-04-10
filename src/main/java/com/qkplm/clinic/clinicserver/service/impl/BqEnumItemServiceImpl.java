/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.BqEnumItemEntity;
import com.qkplm.clinic.clinicserver.mapper.BqEnumItemMapper;
import com.qkplm.clinic.clinicserver.service.IBqEnumItemService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author Wcke
* @description <p>枚举子项表 服务接口类</p>
* @datetime 2024-9-21 9:1
*/
@Service
public class BqEnumItemServiceImpl extends BaqiServiceImpl<BqEnumItemMapper, BqEnumItemEntity> implements IBqEnumItemService {

}
