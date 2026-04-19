/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月18日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qkplm.clinic.clinicserver.dtos.BqPrescriptionFullDto;
import com.qkplm.clinic.clinicserver.dtos.BqSaveMedicalOrderDto;
import com.qkplm.clinic.clinicserver.entity.BqPatientEntity;
import com.qkplm.clinic.clinicserver.entity.BqPrescriptionEntity;
import com.qkplm.clinic.clinicserver.entity.BqPrescriptionItemEntity;
import com.qkplm.clinic.clinicserver.entity.BqRegistrationEntity;
import com.qkplm.clinic.clinicserver.mapper.BqPrescriptionMapper;
import com.qkplm.clinic.clinicserver.service.IBqPatientService;
import com.qkplm.clinic.clinicserver.service.IBqPrescriptionItemService;
import com.qkplm.clinic.clinicserver.service.IBqPrescriptionService;
import com.qkplm.clinic.clinicserver.service.IBqRegistrationService;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author Wcke
* @description <p>处方主表 服务接口类</p>
* @datetime 2026-4-18 0:54
*/
@Service
public class BqPrescriptionServiceImpl extends BaqiServiceImpl<BqPrescriptionMapper, BqPrescriptionEntity> implements IBqPrescriptionService {

    @Autowired
    private IBqPatientService patientService;

    @Autowired
    private IBqRegistrationService registrationService;

    @Autowired
    private IBqPrescriptionItemService itemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BqSaveMedicalOrderDto.Result saveMedicalOrder(BqSaveMedicalOrderDto dto) {
        // 1. 确保患者存在
        Integer patientId = dto.getPatientId();
        if (patientId == null) {
            BqPatientEntity patient = new BqPatientEntity();
            patient.setName(dto.getPatientName());
            patient.setGender(dto.getGender());
            patient.setFirstAge(dto.getFirstAge());
            patient.setLastAge(dto.getLastAge());
            patient.setAgeType(dto.getAgeType());
            patient.setIdCard(dto.getIdCard());
            patient.setMobile(dto.getMobile());
            patient.setProvince(dto.getProvince());
            patient.setCity(dto.getCity());
            patient.setDistrict(dto.getDistrict());
            patient.setAddress(dto.getAddress());
            patient.setIsAllergy(dto.getIsAllergy());
            patient.setAllergicHistory(dto.getAllergicHistory());
            if (!patientService.save(patient)) {
                throw new BQApiException("新增患者失败");
            }
            patientId = patient.getId();
        }

        // 2. 确保挂号存在
        Integer regId = dto.getRegId();
        if (regId == null) {
            BqRegistrationEntity registration = new BqRegistrationEntity();
            registration.setPatientId(patientId);
            registration.setPatient(dto.getPatientName());
            registration.setGender(dto.getGender());
            registration.setFirstAge(dto.getFirstAge());
            registration.setLastAge(dto.getLastAge());
            registration.setAgeType(dto.getAgeType() != null ? dto.getAgeType().byteValue() : null);
            registration.setIsFirstVisit(dto.getIsFirstVisit());
            registration.setStatus("待接诊");
            registration.setOrderTime(LocalDateTime.now());
            if (!registrationService.save(registration)) {
                throw new BQApiException("新增挂号失败");
            }
            regId = registration.getId();
        }

        // 3. 病历ID（null 或 0 均视为无关联病历）
        Integer recordId = (dto.getRecordId() != null && dto.getRecordId() > 0) ? dto.getRecordId() : null;

        // 4. 逐组保存处方主表 + 明细
        List<Integer> prescIds = new ArrayList<>();
        List<BqSaveMedicalOrderDto.Group> groups = dto.getPrescriptions();
        if (groups != null) {
            for (BqSaveMedicalOrderDto.Group group : groups) {
                List<BqPrescriptionItemEntity> items = group.getItems();
                if (items == null || items.isEmpty()) {
                    prescIds.add(group.getPrescId());
                    continue;
                }

                Integer prescId = group.getPrescId();
                if (prescId != null) {
                    // 更新处方主表
                    BqPrescriptionEntity presc = new BqPrescriptionEntity();
                    presc.setId(prescId);
                    presc.setRecordId(recordId);
                    presc.setRegId(regId);
                    presc.setPatientId(patientId);
                    presc.setPrescType(group.getPrescType() != null ? group.getPrescType().byteValue() : null);
                    presc.setGroupNo(group.getGroupNo());
                    presc.setTotalPrice(group.getTotalPrice());
                    if (!updateById(presc)) {
                        throw new BQApiException("更新处方失败");
                    }

                    // 逻辑删除旧明细，重新写入
                    List<BqPrescriptionItemEntity> oldItems = itemService.list(
                            new QueryWrapper<BqPrescriptionItemEntity>().eq("prescId", prescId));
                    if (!oldItems.isEmpty()) {
                        List<Integer> oldIds = oldItems.stream()
                                .map(BqPrescriptionItemEntity::getId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        itemService.removeBatchByIds(oldIds);
                    }
                } else {
                    // 新增处方主表
                    BqPrescriptionEntity presc = new BqPrescriptionEntity();
                    presc.setRecordId(recordId);
                    presc.setRegId(regId);
                    presc.setPatientId(patientId);
                    presc.setPrescType(group.getPrescType() != null ? group.getPrescType().byteValue() : null);
                    presc.setGroupNo(group.getGroupNo());
                    presc.setTotalPrice(group.getTotalPrice());
                    presc.setStatus((byte) 1);
                    if (!save(presc)) {
                        throw new BQApiException("新增处方失败");
                    }
                    prescId = presc.getId();
                }

                // 写入明细
                final Integer finalPrescId = prescId;
                items.forEach(item -> {
                    item.setId(null);
                    item.setPrescId(finalPrescId);
                });
                if (!itemService.saveBatch(items)) {
                    throw new BQApiException("保存处方明细失败");
                }
                prescIds.add(prescId);
            }
        }

        BqSaveMedicalOrderDto.Result result = new BqSaveMedicalOrderDto.Result();
        result.setPatientId(patientId);
        result.setRegId(regId);
        result.setPrescIds(prescIds);
        return result;
    }

    @Override
    public List<BqPrescriptionFullDto> listByRegId(Integer regId) {
        List<BqPrescriptionEntity> prescriptions = list(
                new LambdaQueryWrapper<BqPrescriptionEntity>()
                        .eq(BqPrescriptionEntity::getRegId, regId)
                        .orderByAsc(BqPrescriptionEntity::getId));
        return prescriptions.stream().map(presc -> {
            List<BqPrescriptionItemEntity> items = itemService.list(
                    new LambdaQueryWrapper<BqPrescriptionItemEntity>()
                            .eq(BqPrescriptionItemEntity::getPrescId, presc.getId())
                            .orderByAsc(BqPrescriptionItemEntity::getId));
            BqPrescriptionFullDto dto = new BqPrescriptionFullDto();
            dto.setPrescription(presc);
            dto.setItems(items);
            return dto;
        }).collect(Collectors.toList());
    }
}
