/*
 * 版权声明 Copyright (c) 2026。
 * 版权所有者： [九维无纸化病案管理系统]
 */
package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.qkplm.clinic.clinicserver.dtos.BqPrescriptionPrintVo;
import com.qkplm.clinic.clinicserver.entity.BqPrescriptionItemEntity;
import com.qkplm.clinic.clinicserver.entity.BqMedicalDictionaryEntity;
import com.qkplm.clinic.clinicserver.mapper.BqMedicalDictionaryMapper;
import com.qkplm.clinic.clinicserver.mapper.BqPrescriptionItemMapper;
import com.qkplm.clinic.clinicserver.mapper.BqPrescriptionMapper;
import com.qkplm.clinic.libcommon.pdf.openpdf.ChinesePrescriptionPdfUtil;
import com.qkplm.clinic.libcommon.pdf.openpdf.PrescriptionPdfUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Wcke
 * @description 处方PDF生成服务（基于 OpenPDF 模板）
 */
@Service
@RequiredArgsConstructor
public class BqPrescriptionPrintService {

    private final BqPrescriptionMapper prescriptionMapper;
    private final BqPrescriptionItemMapper itemMapper;
    private final BqMedicalDictionaryMapper dictionaryMapper;

    // ==================== 公开入口 ====================

    /**
     * @param regId        挂号ID
     * @param showPrice    是否显示金额
     * @param printCurrent true=仅打印当前处方类型，false=打印全部
     * @param prescType    处方类型（printCurrent=true 时有效）：1西药 2中药 3检查 4处置
     */
    public byte[] generatePdf(Integer regId, Boolean showPrice,
                               Boolean printCurrent, Integer prescType) throws Exception {
        // 1. 联表查询处方头信息
        List<BqPrescriptionPrintVo> headers = prescriptionMapper.queryForPrint(regId);
        if (headers.isEmpty()) {
            throw new RuntimeException("该挂号暂无处方信息");
        }

        // 2. 打印当前处方：按类型过滤
        if (Boolean.TRUE.equals(printCurrent) && prescType != null) {
            final int pt = prescType;
            headers = headers.stream()
                    .filter(h -> pt == h.getPrescType())
                    .collect(Collectors.toList());
            if (headers.isEmpty()) {
                throw new RuntimeException("该类型暂无处方信息");
            }
        }

        // 3. 批量查询明细，按 prescId 分组
        List<Integer> prescIds = headers.stream()
                .map(BqPrescriptionPrintVo::getId)
                .collect(Collectors.toList());
        List<BqPrescriptionItemEntity> allItems = itemMapper.selectList(
                new LambdaQueryWrapper<BqPrescriptionItemEntity>()
                        .in(BqPrescriptionItemEntity::getPrescId, prescIds)
                        .orderByAsc(BqPrescriptionItemEntity::getPrescId)
                        .orderByAsc(BqPrescriptionItemEntity::getId));
        Map<Integer, List<BqPrescriptionItemEntity>> itemMap = allItems.stream()
                .collect(Collectors.groupingBy(BqPrescriptionItemEntity::getPrescId));

        // 4. 逐张生成 PDF
        boolean show = Boolean.TRUE.equals(showPrice);
        List<byte[]> pdfList = new ArrayList<>();
        for (BqPrescriptionPrintVo header : headers) {
            List<BqPrescriptionItemEntity> items =
                    itemMap.getOrDefault(header.getId(), List.of());
            pdfList.add(buildOnePdf(header, items, show));
        }

        // 5. 单张直接返回，多张合并后返回
        return pdfList.size() == 1 ? pdfList.get(0) : mergePdfs(pdfList);
    }

    // ==================== 单张 PDF 生成 ====================

    private byte[] buildOnePdf(BqPrescriptionPrintVo header,
                                List<BqPrescriptionItemEntity> items,
                                boolean showPrice) {
        // 处方类型 2 = 中药，其余全部走西药模板
        if (Integer.valueOf(2).equals(header.getPrescType())) {
            return ChinesePrescriptionPdfUtil.generatePrescriptionPdf(
                    List.of(buildTcmDto(header, items)), showPrice);
        }
        return PrescriptionPdfUtil.generatePrescriptionPdf(
                List.of(buildWesternDto(header, items)), showPrice);
    }

    // ==================== 西药处方 DTO 构建 ====================

    private PrescriptionPdfUtil.PrescriptionDTO buildWesternDto(
            BqPrescriptionPrintVo h, List<BqPrescriptionItemEntity> items) {

        PrescriptionPdfUtil.PrescriptionDTO dto = new PrescriptionPdfUtil.PrescriptionDTO();
        dto.setPrescNo(h.getPrescNo());
        dto.setPatientName(h.getPatientName());
        dto.setGender(h.getGender());
        dto.setAge(h.getAge());
        dto.setFirstAge(h.getFirstAge());
        dto.setAgeType(h.getAgeType() != null ? String.valueOf(h.getAgeType()) : null);
        dto.setIdCard(h.getIdCard());
        dto.setClinicNo(clinicNo(h));
        dto.setPhone(h.getPhone());
        dto.setOrderTime(h.getOrderTime());
        dto.setAddress(h.getAddress());
        dto.setAllergicHistory(h.getAllergicHistory());
        dto.setDiagnosis(h.getDiagnosis());
        dto.setTreatmentSuggest(h.getTreatmentSuggest());
        dto.setDoctor(h.getDoctor());
        dto.setTotalPrice(h.getTotalPrice());

        Map<String, String> unitMap = buildUnitMap();
        dto.setItems(items.stream().map(item -> {
            PrescriptionPdfUtil.PrescriptionItemDTO d = new PrescriptionPdfUtil.PrescriptionItemDTO();
            d.setItemType(item.getItemType());
            d.setGroupNo(item.getGroupNo());
            d.setItemName(item.getItemName());
            d.setSpec(item.getSpec());
            d.setTotalNum(item.getTotalNum());
            d.setUseWay(item.getUseWay());
            d.setSingleDosage(item.getSingleDosage());
            d.setUnit(resolveUnit(item.getUnit(), item.getUnitId(), unitMap));
            d.setFrequency(item.getFrequency());
            d.setDays(item.getDays());
            d.setEntrust(item.getEntrust());
            return d;
        }).collect(Collectors.toList()));

        return dto;
    }

    // ==================== 中药处方 DTO 构建 ====================

    private ChinesePrescriptionPdfUtil.PrescriptionDTO buildTcmDto(
            BqPrescriptionPrintVo h, List<BqPrescriptionItemEntity> items) {

        ChinesePrescriptionPdfUtil.PrescriptionDTO dto = new ChinesePrescriptionPdfUtil.PrescriptionDTO();
        dto.setPrescNo(h.getPrescNo());
        dto.setPatientName(h.getPatientName());
        dto.setGender(h.getGender());
        dto.setAge(h.getAge());
        dto.setFirstAge(h.getFirstAge());
        dto.setAgeType(h.getAgeType() != null ? String.valueOf(h.getAgeType()) : null);
        dto.setIdCard(h.getIdCard());
        dto.setClinicNo(clinicNo(h));
        dto.setPhone(h.getPhone());
        dto.setOrderTime(h.getOrderTime());
        dto.setAddress(h.getAddress());
        dto.setAllergicHistory(h.getAllergicHistory());
        dto.setDiagnosis(h.getDiagnosis());
        dto.setTreatmentSuggest(h.getTreatmentSuggest());
        dto.setDoctor(h.getDoctor());
        dto.setTotalPrice(h.getTotalPrice());

        // 中药专有字段
        // 总剂数行：剂数 + 频率名称 + 天数，如 "10剂    每日两次（bid）    共5天"
        String doseStr = h.getDoseAmount() != null ? h.getDoseAmount() + "剂" : "";
        String freqStr = nvl(h.getFrequenceName());
        String daysStr = h.getDays() != null ? "共" + h.getDays() + "天" : "";
        String totalDosageLine = doseStr;
        if (!freqStr.isEmpty()) totalDosageLine += "    " + freqStr;
        if (!daysStr.isEmpty()) totalDosageLine += "    " + daysStr;
        dto.setTotalDosage(totalDosageLine);
        // 用法行：取 usageType 对应的中文名称
        dto.setDailyDosage(nvl(h.getUsageTypeName()));
        dto.setMedicalAdvice(h.getRecommendation());

        Map<String, String> unitMap = buildUnitMap();
        Map<String, String> decoWayMap = dictionaryMapper.selectList(
                new LambdaQueryWrapper<BqMedicalDictionaryEntity>()
                        .eq(BqMedicalDictionaryEntity::getDictType, 5))
                .stream()
                .collect(Collectors.toMap(
                        e -> String.valueOf(e.getId()),
                        BqMedicalDictionaryEntity::getName,
                        (a, b) -> a));

        dto.setItems(items.stream().map(item -> {
            ChinesePrescriptionPdfUtil.PrescriptionItemDTO d =
                    new ChinesePrescriptionPdfUtil.PrescriptionItemDTO();
            d.setItemType(item.getItemType());
            d.setItemName(item.getItemName());
            // 中药剂量 = 单次用量 + 单位（如 "10" + "g" = "10g"）
            d.setDosage(trimNumber(item.getSingleDosage()) + resolveUnit(item.getUnit(), item.getUnitId(), unitMap));
            d.setDecoctWay(decoWayMap.getOrDefault(nvl(item.getDecoWay()), ""));
            return d;
        }).collect(Collectors.toList()));

        return dto;
    }

    // ==================== PDF 合并（不处理页码，仅拼接页面） ====================

    private byte[] mergePdfs(List<byte[]> pdfList) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfCopy copy = new PdfCopy(document, out);
        document.open();
        for (byte[] pdfBytes : pdfList) {
            PdfReader reader = new PdfReader(pdfBytes);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                copy.addPage(copy.getImportedPage(reader, i));
            }
            reader.close();
        }
        document.close();
        return out.toByteArray();
    }

    // ==================== 工具方法 ====================

/** 门诊编号：取患者档案号 */
    private String clinicNo(BqPrescriptionPrintVo h) {
        return h.getArchiveNo() != null ? h.getArchiveNo() : "";
    }

    private Map<String, String> buildUnitMap() {
        return dictionaryMapper.selectList(
                new LambdaQueryWrapper<BqMedicalDictionaryEntity>()
                        .eq(BqMedicalDictionaryEntity::getDictType, 3))
                .stream()
                .collect(Collectors.toMap(
                        e -> String.valueOf(e.getId()),
                        BqMedicalDictionaryEntity::getName,
                        (a, b) -> a));
    }

    private String resolveUnit(String unit, Integer unitId, Map<String, String> unitMap) {
        if (unit != null && !unit.isEmpty()) return unit;
        if (unitId == null) return "";
        return unitMap.getOrDefault(String.valueOf(unitId), "");
    }

    private String trimNumber(String s) {
        if (s == null || s.isEmpty()) return s == null ? "" : s;
        try {
            return new java.math.BigDecimal(s).stripTrailingZeros().toPlainString();
        } catch (NumberFormatException e) {
            return s;
        }
    }

    private String nvl(String s) {
        return s == null ? "" : s;
    }
}
