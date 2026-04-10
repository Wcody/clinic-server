/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.qkplm.clinic.clinicserver.dtos.BQRecordDto;
import com.qkplm.clinic.clinicserver.entity.BqParamItemEntity;
import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;
import com.qkplm.clinic.clinicserver.mapper.TbRecordMapper;
import com.qkplm.clinic.clinicserver.service.*;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
* @author Wcke
* @description <p>病案表 服务接口类</p>
* @datetime 2024-9-21 9:1
*/
@Slf4j
@Service
public class TbRecordServiceImpl extends BaqiServiceImpl<TbRecordMapper, TbRecordEntity> implements ITbRecordService {
    private final ITbRecordTrackService recordTrackService;
    private final IBqParamItemService paramItemService;
    private final ITbBbsService bbsService;

    public TbRecordServiceImpl(ITbRecordTrackService recordTrackService, IBqParamItemService paramItemService, ITbBbsService bbsService) {
        this.recordTrackService = recordTrackService;
        this.paramItemService = paramItemService;
        this.bbsService = bbsService;
    }

    @Override
    public boolean save(TbRecordEntity entity) {
        boolean result = super.save(entity);
        addRecordTrack(entity);
        return result;
    }

    @Override
    public boolean saveBatch(Collection<TbRecordEntity> entityList) {
        boolean result = super.saveBatch(entityList);
        entityList.forEach(this::addRecordTrack);
        return result;
    }

    /**
     * 添加示踪记录
     */
    private void addRecordTrack(TbRecordEntity entity) {
        recordTrackService.addInsertTrackBy(entity);
    }

    @Override
    public boolean nurseQc(String recordId, String content) {
        TbRecordEntity entity = this.getById(recordId);
        if (Objects.isNull(entity)) {
            throw new BQApiException("病案不存在");
        }
        if (entity.getHasNurse()) {
            throw new BQApiException("病案已护士质控");
        }
        recordTrackService.addTrack(recordId, "护士质控，理由：" + content);
        entity.setHasNurse(true);
        return updateById(entity);
    }

    @Override
    public boolean doctorQc(String recordId, String content) {
        TbRecordEntity entity = this.getById(recordId);
        if (Objects.isNull(entity)) {
            throw new BQApiException("病案不存在");
        }
        if (entity.getHasDoctor()) {
            throw new BQApiException("病案已医生质控");
        }
        recordTrackService.addTrack(recordId, "医生质控，理由：" + content);
        entity.setHasDoctor(true);
        return updateById(entity);
    }

    @Override
    public boolean finalQc(String recordId, String content) {
        TbRecordEntity entity = this.getById(recordId);
        if (Objects.isNull(entity)) {
            throw new BQApiException("病案不存在");
        }
        if (entity.getHasFinal()) {
            throw new BQApiException("病案已终末质控");
        }
        recordTrackService.addTrack(recordId, "终末质控，理由：" + content);
        entity.setHasFinal(true);
        entity.setHasArchived(true);
        return updateById(entity);
    }

    @Override
    public boolean doctorQcb(String recordId, String content) {
        TbRecordEntity entity = this.getById(recordId);
        if (Objects.isNull(entity)) {
            throw new BQApiException("病案不存在");
        }
        if (entity.getHasFinal()) {
            throw new BQApiException("病案已终末质控");
        }
        if (!entity.getHasNurse()) {
            throw new BQApiException("护士未质控，无法退回");
        }
        recordTrackService.addTrack(recordId, "医生质控退回护士质控，理由：" + content);
        entity.setHasNurse(false);
        return updateById(entity);
    }

    @Override
    public boolean finalQcb(String recordId, String content) {
        TbRecordEntity entity = this.getById(recordId);
        if (Objects.isNull(entity)) {
            throw new BQApiException("病案不存在");
        }
        if (entity.getHasFinal()) {
            throw new BQApiException("病案已终末质控");
        }
        if (!entity.getHasDoctor()) {
            throw new BQApiException("医生未质控，无法退回");
        }
        recordTrackService.addTrack(recordId, "终末质控退回医生质控，理由：" + content);
        entity.setHasDoctor(false);
        return updateById(entity);
    }

    @Override
    public TbRecordEntity getByRecordCode(String recordCode) {
        return getOne(new LambdaQueryWrapper<TbRecordEntity>().eq(TbRecordEntity::getRecordCode, recordCode));
    }

    @Override
    public TbRecordEntity getByPatientIdAndName(String patientId, String patientName) {
        return getOne(new LambdaQueryWrapper<TbRecordEntity>().eq(TbRecordEntity::getPatientId, patientId).eq(TbRecordEntity::getPatientName, patientName));
    }

    private HashMap<String, String> addItem(String value, String label) {
        return new HashMap<>() {{
            put("value", value);
            put("label", label);
        }};
    }

    @Override
    public List<HashMap<String, String>> getFieldOptions(Integer recordKind) {
        if (recordKind == 1) {
            return List.of(addItem("hospitalName", "医院名称"),
                    addItem("creditCode", "统一社会信用代码"),
                    addItem("recordCode", "病案号"),
                    addItem("admissionCount", "住院次数"),
                    addItem("admissionDate", "入院时间"),
                    addItem("dischargeDate", "出院时间"),
                    addItem("patientCode", "健康卡号"),
                    addItem("payType", "医疗付费方式"),
                    addItem("patientName", "姓名"),
                    addItem("gender", "性别"),
                    addItem("birthDate", "出生日期"),
                    addItem("ageYear", "年龄（岁）"),
                    addItem("ageDay", "年龄（天）"),
                    addItem("maritalStatus", "婚姻状态"),
                    addItem("nationality", "国籍"),
                    addItem("ethnicity", "民族"),
                    addItem("idType", "证件类型"),
                    addItem("idCard", "证件号码"),
                    addItem("birthAddress", "出生地址"),
                    addItem("nativeProvince", "籍贯省（自治区、直辖市）"),
                    addItem("hukouAddress", "户口地址"),
                    addItem("hukouZipCode", "户口地址邮政编码"),
                    addItem("liveAddress", "现住址"),
                    addItem("livePhone", "现住址电话"),
                    addItem("liveZipCode", "现住址邮编"),
                    addItem("workUnitAddress", "工作单位及地址"),
                    addItem("workUnitPhone", "工作单位电话"),
                    addItem("workUnitZipCode", "工作单位邮政编码"),
                    addItem("contactName", "联系人姓名"),
                    addItem("contactRelationship", "联系人关系"),
                    addItem("contactAddress", "联系人地址"),
                    addItem("contactPhone", "联系电话"),
                    addItem("hasDaySurgery", "是否为日间手术"),
                    addItem("admissionRoute", "入院途径"),
                    addItem("admissionDepartment", "入院科别"),
                    addItem("admissionWard", "入院病房"),
                    addItem("transferDepartment", "转科科别"),
                    addItem("dischargeDepartment", "出院科别"),
                    addItem("dischargeWard", "出院病房"),
                    addItem("actualHospitalizationDays", "实际住院（天）"),
                    addItem("admissionCondition", "入院时情况"),
                    addItem("admissionDiagnosisCode", "入院诊断编码"),
                    addItem("admissionDiagnosisName", "入院诊断名称"),
                    addItem("postAdmissionConfirmationDate", "入院后确诊日期"),
                    addItem("primaryDiagnosisCode", "出院主要诊断编码"),
                    addItem("primaryDiagnosisName", "出院主要诊断名称"),
                    addItem("primaryDiagnosisAdmissionCondition", "出院主要诊断入院病情"),
                    addItem("primaryDiagnosisDischargeCondition", "主要诊断出院情况"),
                    addItem("pathologyDiagnosisCode", "病理诊断编码"),
                    addItem("pathologyDiagnosisName", "病理诊断名称"),
                    addItem("pathologyNumber", "病理号"),
                    addItem("externalCauseOfInjuryOrPoisoningCode", "损伤、中毒外部原因编码"),
                    addItem("externalCauseOfInjuryOrPoisoningName", "损伤、中毒外部原因名称"),
                    addItem("drugAllergyHistory", "有无药物过敏史"),
                    addItem("allergyDrugs", "药物过敏名称"),
                    addItem("nbsag", "HBsAg"),
                    addItem("hcvab", "HCV-Ab"),
                    addItem("hivab", "HIV-Ab"),
                    addItem("departmentHeadCode", "科主任编码"),
                    addItem("departmentHead", "科主任"),
                    addItem("attendingPhysicianCode", "主（副主）任医师编码"),
                    addItem("attendingPhysician", "主（副主）任医师"),
                    addItem("associatePhysicianCode", "主治医师编码"),
                    addItem("associatePhysician", "主治医师"),
                    addItem("residentPhysicianCode", "住院医师编码"),
                    addItem("residentPhysician", "住院医师"),
                    addItem("responsibleNurseCode", "责任护士编码"),
                    addItem("responsibleNurse", "责任护士"),
                    addItem("visitingPhysician", "进修医师"),
                    addItem("internPhysician", "实习医师"),
                    addItem("coder", "编码员"),
                    addItem("medicalRecordQuality", "病案质量"),
                    addItem("qualityControlPhysician", "质控医师"),
                    addItem("qualityControlNurse", "质控护师"),
                    addItem("qualityControlDate", "质控日期"),
                    addItem("autopsyForDeceased", "死亡患者尸检"),
                    addItem("aboBloodType", "ABO血型"),
                    addItem("rhBloodType", "Rh血型"),
                    addItem("intensiveCareDays", "特级护理天数"),
                    addItem("firstLevelCareDays", "一级护理天数"),
                    addItem("secondLevelCareDays", "二级护理天数"),
                    addItem("thirdLevelCareDays", "三级护理天数"),
                    addItem("bloodTransfusionReaction", "输血反应"),
                    addItem("redBloodCells", "红细胞"),
                    addItem("platelets", "血小板"),
                    addItem("plasma", "血浆"),
                    addItem("wholeBlood", "全血"),
                    addItem("autologousBloodTransfusion", "自体血回输"),
                    addItem("A18x01", "新生儿出生体重（克）1"),
                    addItem("A18x02", "新生儿出生体重（克）2"),
                    addItem("A18x03", "新生儿出生体重（克）3"),
                    addItem("A18x04", "新生儿出生体重（克）4"),
                    addItem("A18x05", "新生儿出生体重（克）5"),
                    addItem("A17", "新生儿入院体重（克）"),
                    addItem("C28", "颅脑损伤患者入院前昏迷时间（天）"),
                    addItem("C29", "颅脑损伤患者入院前昏迷时间（小时）"),
                    addItem("C30", "颅脑损伤患者入院前昏迷时间（分钟）"),
                    addItem("C31", "颅脑损伤患者入院后昏迷时间（天）"),
                    addItem("C32", "颅脑损伤患者入院后昏迷时间（小时）"),
                    addItem("C33", "颅脑损伤患者入院后昏迷时间（分钟）"),
                    addItem("C47", "有创呼吸机使用时间"),
                    addItem("B36C", "是否有出院31日内再住院计划"),
                    addItem("B37", "出院31天再住院计划目的"),
                    addItem("B34C", "离院方式"),
                    addItem("B35", "医嘱转院、转社区卫生服务机构/乡镇卫生院名称"),
                    addItem("totalCost", "住院总费用"),
                    addItem("selfPaidCost", "其中，自付金额"),
                    addItem("generalServiceCost", "1.一般医疗服务费"),
                    addItem("generalTreatmentCost", "2.一般治疗操作费"),
                    addItem("nursingCost", "3.护理费"),
                    addItem("otherServiceCost", "4.综合医疗服务类其他费用"),
                    addItem("pathologyCost", "5.病理诊断费"),
                    addItem("labTestCost", "6.实验室诊断费"),
                    addItem("imagingCost", "7.影像学诊断费"),
                    addItem("clinicalCost", "8.临床诊断项目费"),
                    addItem("nonSurgicalCost", "9.非手术治疗项目费"),
                    addItem("physicalTherapyCost", "其中：临床物理治疗费"),
                    addItem("surgeryTotalCost", "10.手术治疗费"),
                    addItem("anesthesiaCost", "其中：麻醉费"),
                    addItem("surgeryCost", "其中：手术费"),
                    addItem("recoveryCost", "11.康复费"),
                    addItem("chineseMedicineCost", "12.中医治疗费"),
                    addItem("westernMedicineCost", "13.西药费"),
                    addItem("antibioticCost", "其中：抗菌药物费用"),
                    addItem("chinesePrepCost", "14.中成药费"),
                    addItem("herbalMedicineCost", "15.中草药费"),
                    addItem("bloodCost", "16.血费"),
                    addItem("albuminCost", "17.白蛋白类制品费"),
                    addItem("globulinCost", "18.球蛋白类制品费"),
                    addItem("coagulationCost", "19.凝血因子类制品费"),
                    addItem("cytokineCost", "20.细胞因子类制品费"),
                    addItem("disposableExamCost", "21.检查用一次性医用材料费"),
                    addItem("disposableTreatmentCost", "22.治疗用一次性医用材料费"),
                    addItem("disposableSurgeryCost", "23.手术用一次性医用材料费"),
                    addItem("adNumber", "住院号"),
                    addItem("pageNumber", "页码"),
                    addItem("otherCost", "24.其他费："));

        } else if (recordKind == 2) {
            return List.of(addItem("hospitalName", "医院名称"),
                    addItem("creditCode", "统一社会信用代码"),
                    addItem("recordCode", "病案号"),
                    addItem("dischargeDate", "出院时间"),
                    addItem("patientCode", "健康卡号"),
                    addItem("payType", "医疗付费方式"),
                    addItem("patientName", "姓名"),
                    addItem("gender", "性别"),
                    addItem("birthDate", "出生日期"),
                    addItem("ageYear", "年龄（岁）"),
                    addItem("ageDay", "年龄（天）"),
                    addItem("maritalStatus", "婚姻状态"),
                    addItem("nationality", "国籍"),
                    addItem("ethnicity", "民族"),
                    addItem("idType", "证件类型"),
                    addItem("idCard", "证件号码"),
                    addItem("nativeProvince", "籍贯省（自治区、直辖市）"),
                    addItem("liveAddress", "现住址"),
                    addItem("livePhone", "现住址电话"),
                    addItem("liveZipCode", "现住址邮编"),
                    addItem("drugAllergyHistory", "有无药物过敏史"),
                    addItem("allergyDrugs", "药物过敏名称"),
                    addItem("medicalRecordQuality", "病案质量"),
                    addItem("qualityControlPhysician", "质控医师"),
                    addItem("qualityControlNurse", "质控护师"),
                    addItem("qualityControlDate", "质控日期"),
                    addItem("A18x01", "新生儿出生体重（克）1"),
                    addItem("A18x02", "新生儿出生体重（克）2"),
                    addItem("A18x03", "新生儿出生体重（克）3"),
                    addItem("A18x04", "新生儿出生体重（克）4"),
                    addItem("A18x05", "新生儿出生体重（克）5"),
                    addItem("A17", "新生儿入院体重（克）"),
                    addItem("B34C", "离院方式"),
                    addItem("B35", "医嘱转院、转社区卫生服务机构/乡镇卫生院名称"),
                    addItem("registrationTime", "挂号时间"),
                    addItem("checkInTime", "报到时间"),
                    addItem("visitTime", "就诊时间"),
                    addItem("department", "就诊科室"),
                    addItem("attendingPhysician", "接诊医师"),
                    addItem("physicianTitle", "接诊医师职称"),
                    addItem("visitType", "就诊类型"),
                    addItem("isRevisit", "是否复诊"),
                    addItem("isInfusion", "是否输液"),
                    addItem("isSpecialPatient", "是否为门诊慢特病患者"),
                    addItem("emergencyLevel", "急诊患者分级"),
                    addItem("emergencyOutcome", "急诊患者去向"),
                    addItem("admissionCertTime", "住院证开具时间"),
                    addItem("chiefComplaint", "患者主诉"),
                    addItem("mainDiagnosis", "门（急）诊主要诊断"),
                    addItem("mainDiagnosisCode", "门（急）诊主要诊断编码"),
                    addItem("otherDiagnosis", "门（急）诊其他诊断"),
                    addItem("otherDiagnosisCode", "门（急）诊其他诊断编码"),
                    addItem("totalCost", "门（急）诊总费用"),
                    addItem("selfPaidCost", "其中，自付金额"),
                    addItem("generalServiceCost", "1.一般医疗服务费"),
                    addItem("generalTreatmentCost", "2.一般治疗操作费"),
                    addItem("nursingCost", "3.护理费"),
                    addItem("otherServiceCost", "4.综合医疗服务类其他费用"),
                    addItem("pathologyCost", "5.病理诊断费"),
                    addItem("labTestCost", "6.实验室诊断费"),
                    addItem("imagingCost", "7.影像学诊断费"),
                    addItem("clinicalCost", "8.临床诊断项目费"),
                    addItem("nonSurgicalCost", "9.非手术治疗项目费"),
                    addItem("physicalTherapyCost", "其中：临床物理治疗费"),
                    addItem("surgeryTotalCost", "10.手术治疗费"),
                    addItem("anesthesiaCost", "其中：麻醉费"),
                    addItem("surgeryCost", "其中：手术费"),
                    addItem("recoveryCost", "11.康复费"),
                    addItem("chineseMedicineCost", "12.中医治疗费"),
                    addItem("westernMedicineCost", "13.西药费"),
                    addItem("antibioticCost", "其中：抗菌药物费用"),
                    addItem("chinesePrepCost", "14.中成药费"),
                    addItem("herbalMedicineCost", "15.中草药费"),
                    addItem("bloodCost", "16.血费"),
                    addItem("albuminCost", "17.白蛋白类制品费"),
                    addItem("globulinCost", "18.球蛋白类制品费"),
                    addItem("coagulationCost", "19.凝血因子类制品费"),
                    addItem("cytokineCost", "20.细胞因子类制品费"),
                    addItem("disposableExamCost", "21.检查用一次性医用材料费"),
                    addItem("disposableTreatmentCost", "22.治疗用一次性医用材料费"),
                    addItem("disposableSurgeryCost", "23.手术用一次性医用材料费"),
                    addItem("adNumber", "门诊号"),
                    addItem("pageNumber", "页码"),
                    addItem("otherCost", "24.其他费："));
        } else {
            return List.of(addItem("hospitalName", "医院名称"),
                    addItem("creditCode", "统一社会信用代码"),
                    addItem("adNumber", "文件编号"),
                    addItem("pageNumber", "页码"),
                    addItem("recordCode", "档案号"));
        }
    }

    @Override
    public List<BQRecordDto> listWz(HashMap<String, Object> params) {
        log.info("params:{}", BQJacksonUtils.toJson(params));
        // 这个参数是必须指定的
        Integer recordKind = (Integer) params.get("recordKind");
        if (Objects.isNull(recordKind)) {
            throw new BQApiException("recordKind不能为空");
        }

        if (recordKind == -1) {
            log.warn("listWz的recordKind参数非法，当前值为：{}", recordKind);
            return new ArrayList<>();
        }

        Integer paramId = List.of(13, 11, 12).get(recordKind);
        BqParamItemEntity paramItem = paramItemService.getById(paramId);
        if (Objects.isNull(paramItem)) {
            throw new BQApiException("完整性参数项不存在");
        }

        ObjectNode paramValue = paramItemService.getRealParamData(paramId);
        //ObjectNode paramValue = paramItem.getParamValue();
        if (Objects.isNull(paramValue)) {
            throw new BQApiException("完整性参数项值不存在");
        }

        log.info("paramValue:{}", BQJacksonUtils.toJson(paramValue));
        JsonNode node = paramValue.get("status");
        if (Objects.isNull(node) || !node.asBoolean()) {
            log.info("完整性参数未开启");
            return new ArrayList<>();
        }

        JsonNode dataListNode = paramValue.get("dataList");
        if (Objects.isNull(dataListNode) || (dataListNode.isArray() && dataListNode.isEmpty())) {
            log.info("完整性规则参数未设置");
            return new ArrayList<>();
        }

        // 构造cte
        StringBuilder builder = new StringBuilder();
        builder.append("with cte as (select * from tb_record where recordKind = ").append(recordKind);
        String recordId = (String) params.get("recordId");
        if (StringUtils.isNotBlank(recordId)) {
            builder.append(" and eid = '").append(recordId).append("'");
        }
        builder.append(") ");

        // 构造规则union 子句
        boolean isFirst = false;
        ArrayNode dataList = (ArrayNode) dataListNode;
        for (int i = 0; i < dataList.size(); i++) {
            ObjectNode data = (ObjectNode) dataList.get(i);
            if (data.get("status").asBoolean()) {
                if (!isFirst) {
                    isFirst = true;
                } else {
                    builder.append(" union all ");
                }
                builder.append("select '").append(data.get("ruleDesc").asText()).append("' ruleDesc,");
                builder.append(data.get("ruleKind").asInt()).append(" ruleKind,");
                builder.append(data.get("limitKind").asInt()).append(" limitKind");
                builder.append(", cte.* from cte");
                builder.append(" where ").append(data.get("ruleContent").asText());
            }
        }

        // 构造子句 where

        String sql = builder.toString();
        log.info("listWz sql:{}", sql);
        return baseMapper.listWz(sql);
    }

    @Override
    public List<HashMap<String, Object>> listRank() {
        // 今天最早的时间
        LocalDateTime startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        // 今天最晚的时间
        LocalDateTime endTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(0);
        // 根据时间范围获取
        return listRank(startTime, endTime);
    }

    @Override
    public List<HashMap<String, Object>> listRank(LocalDateTime startTime, LocalDateTime endTime) {
        return List.of(new HashMap<>());
    }
}
