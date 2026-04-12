/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月9日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.dtos.DiagnosisDictListDto;
import com.qkplm.clinic.clinicserver.dtos.DiseaseDetailNewDto;
import com.qkplm.clinic.clinicserver.entity.TbDiagnosisDictEntity;
import com.qkplm.clinic.clinicserver.mapper.SysTcmDeptMapper;
import com.qkplm.clinic.clinicserver.mapper.SysTcmDiseaseMapper;
import com.qkplm.clinic.clinicserver.mapper.SysTcmExpertPrescriptionMapper;
import com.qkplm.clinic.clinicserver.mapper.SysTcmPrescriptionMapper;
import com.qkplm.clinic.clinicserver.mapper.SysTcmSyndromeMapper;
import com.qkplm.clinic.clinicserver.mapper.TbDiagnosisDictMapper;
import com.qkplm.clinic.clinicserver.service.ITbDiagnosisDictService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author Wcke
* @description <p>诊断字典表 服务接口类</p>
* @datetime 2026-4-9 11:43
*/
@Service
public class TbDiagnosisDictServiceImpl extends BaqiServiceImpl<TbDiagnosisDictMapper, TbDiagnosisDictEntity> implements ITbDiagnosisDictService {

    private final SysTcmDiseaseMapper diseaseMapper;
    private final SysTcmDeptMapper deptMapper;
    private final SysTcmSyndromeMapper syndromeMapper;
    private final SysTcmPrescriptionMapper prescriptionMapper;
    private final SysTcmExpertPrescriptionMapper expertPrescriptionMapper;

    public TbDiagnosisDictServiceImpl(SysTcmDiseaseMapper diseaseMapper,
                                      SysTcmDeptMapper deptMapper,
                                      SysTcmSyndromeMapper syndromeMapper,
                                      SysTcmPrescriptionMapper prescriptionMapper,
                                      SysTcmExpertPrescriptionMapper expertPrescriptionMapper) {
        this.diseaseMapper = diseaseMapper;
        this.deptMapper = deptMapper;
        this.syndromeMapper = syndromeMapper;
        this.prescriptionMapper = prescriptionMapper;
        this.expertPrescriptionMapper = expertPrescriptionMapper;
    }

    @Override
    public DiagnosisDictListDto getDictList() {
        // 1. 查询所有疾病(已包含deptId和firstLetter)
        List<Map<String, Object>> diseases = diseaseMapper.selectAllDiseases();

        // 2. 构建疾病列表
        List<DiagnosisDictListDto.DiseaseItem> diseaseItems = diseases.stream()
            .map(disease -> {
                Long diseaseId = ((Number) disease.get("id")).longValue();
                String alphabet = (String) disease.get("alphabet");
                Integer categoryId = disease.get("category") != null ? 
                    ((Number) disease.get("category")).intValue() : null;
                String pinyin = (String) disease.get("pinyin");
                String pinyinInitial = (String) disease.get("pinyinInitial");
                
                return DiagnosisDictListDto.DiseaseItem.builder()
                    .id(diseaseId)
                    .name((String) disease.get("diseaseName"))
                    .category(categoryId)
                    .alphabet(alphabet)
                    .pinyin(pinyin)
                    .pinyinInitial(pinyinInitial)
                    .build();
            })
            .collect(Collectors.toList());

        // 3. 提取不重复的首字母,并按字母排序
        List<DiagnosisDictListDto.AlphabetItem> alphabetItems = diseases.stream()
            .map(disease -> (String) disease.get("alphabet"))
            .filter(Objects::nonNull)
            .distinct()
            .sorted()
            .map(letter -> DiagnosisDictListDto.AlphabetItem.builder()
                .id(letter)
                .name(letter)
                .build()
            )
            .collect(Collectors.toList());

        // 4. 查询所有科室,按sort排序
        List<Map<String, Object>> depts = deptMapper.selectAllDepts();
        
        List<DiagnosisDictListDto.CategoryItem> categoryItems = depts.stream()
            .map(dept -> DiagnosisDictListDto.CategoryItem.builder()
                .id(((Number) dept.get("id")).intValue())
                .name((String) dept.get("deptName"))
                .build()
            )
            .collect(Collectors.toList());

        // 5. 组装返回结果
        return DiagnosisDictListDto.builder()
            .diseases(diseaseItems)
            .alphabets(alphabetItems)
            .categories(categoryItems)
            .build();
    }

    @Override
    public DiseaseDetailNewDto getDetail(Long id) {
        // 1. 查询疾病基本信息
        Map<String, Object> disease = diseaseMapper.selectById(id);
        if (disease == null) {
            return null;
        }

        Long diseaseId = ((Number) disease.get("id")).longValue();

        // 2. 构建基本信息
        DiseaseDetailNewDto.DiseaseDetailNewDtoBuilder builder = DiseaseDetailNewDto.builder()
            .name((String) disease.get("diseaseName"))
            .summary((String) disease.get("summary"));

        // 3. 查询辨证治疗
        List<Map<String, Object>> syndromes = syndromeMapper.selectByDiseaseId(diseaseId);
        List<DiseaseDetailNewDto.CureItem> cureItems = syndromes.stream()
            .map(syndrome -> {
                Long syndromeId = ((Number) syndrome.get("id")).longValue();
                
                // 查询该证型对应的方剂
                List<Map<String, Object>> prescriptions = prescriptionMapper.selectBySyndromeId(syndromeId);
                
                // 取第一个方剂作为推荐方剂(如果有多个)
                DiseaseDetailNewDto.Prescription recommendPrescription = null;
                Map<String, Object> firstPresc = null;
                if (!prescriptions.isEmpty()) {
                    firstPresc = prescriptions.get(0);
                    Long basicPrescId = firstPresc.get("basicPrescId") != null ? 
                        ((Number) firstPresc.get("basicPrescId")).longValue() : null;
                    
                    if (basicPrescId != null) {
                        // 查询药物组成
                        List<Map<String, Object>> ingredients = prescriptionMapper.selectIngredientsByPrescId(basicPrescId);
                        List<DiseaseDetailNewDto.HerbItem> herbItems = ingredients.stream()
                            .map(ing -> {
                                String dosage = (String) ing.get("dosage");
                                // 解析剂量,如"10g" -> amount=10, unit="g"
                                Double amount = null;
                                String unit = "g";
                                if (dosage != null && !dosage.isEmpty()) {
                                    try {
                                        String numStr = dosage.replaceAll("[^0-9.]", "");
                                        if (!numStr.isEmpty()) {
                                            amount = Double.parseDouble(numStr);
                                        }
                                        unit = dosage.replaceAll("[0-9.]", "");
                                        if (unit.isEmpty()) {
                                            unit = "g";
                                        }
                                    } catch (Exception e) {
                                        // 解析失败,使用默认值
                                    }
                                }
                                
                                return DiseaseDetailNewDto.HerbItem.builder()
                                    .name((String) ing.get("herbName"))
                                    .amount(amount)
                                    .unit(unit)
                                    .remark(dosage)
                                    .cook((String) ing.get("note"))
                                    .prescriptionType(0)
                                    .build();
                            })
                            .collect(Collectors.toList());
                        
                        recommendPrescription = DiseaseDetailNewDto.Prescription.builder()
                            .name((String) firstPresc.get("prescName"))
                            .base(herbItems)
                            .build();
                    }
                }
                
                // 加减法说明(从adjustment字段获取,可能需要拆分)
                String adjustment = firstPresc != null ? (String) firstPresc.get("adjustment") : null;
                List<String> additional = new ArrayList<>();
                if (adjustment != null && !adjustment.isEmpty()) {
                    // 按句号或分号拆分
                    String[] parts = adjustment.split("[。;；]");
                    for (String part : parts) {
                        String trimmed = part.trim();
                        if (!trimmed.isEmpty()) {
                            additional.add(trimmed);
                        }
                    }
                }
                
                return DiseaseDetailNewDto.CureItem.builder()
                    .id(syndromeId)
                    .name((String) syndrome.get("syndromeName"))
                    .spec((String) syndrome.get("syndromeFeature"))
                    .solution((String) syndrome.get("treatmentMethod"))
                    .recommend(recommendPrescription)
                    .additional(additional)
                    .build();
            })
            .collect(Collectors.toList());

        DiseaseDetailNewDto.CureData cureData = DiseaseDetailNewDto.CureData.builder()
            .data(cureItems)
            .desc(null) // TODO: 如果需要可以添加总体说明
            .build();

        // 4. 查询名医经验
        List<Map<String, Object>> expertPrescs = expertPrescriptionMapper.selectByDiseaseId(diseaseId);
        List<DiseaseDetailNewDto.ExpItem> expItems = expertPrescs.stream()
            .map(exp -> {
                Long expId = ((Number) exp.get("id")).longValue();
                
                // 查询验方药物明细
                List<Map<String, Object>> items = expertPrescriptionMapper.selectItemsByExpertPrescId(expId);
                List<DiseaseDetailNewDto.HerbItem> herbItems = items.stream()
                    .map(item -> {
                        String dosage = (String) item.get("dosage");
                        Double amount = null;
                        String unit = "g";
                        if (dosage != null && !dosage.isEmpty()) {
                            try {
                                String numStr = dosage.replaceAll("[^0-9.]", "");
                                if (!numStr.isEmpty()) {
                                    amount = Double.parseDouble(numStr);
                                }
                                unit = dosage.replaceAll("[0-9.]", "");
                                if (unit.isEmpty()) {
                                    unit = "g";
                                }
                            } catch (Exception e) {
                                // 解析失败
                            }
                        }
                        
                        return DiseaseDetailNewDto.HerbItem.builder()
                            .name((String) item.get("herbName"))
                            .amount(amount)
                            .unit(unit)
                            .remark(dosage)
                            .cook((String) item.get("note"))
                            .prescriptionType(0)
                            .build();
                    })
                    .collect(Collectors.toList());
                
                DiseaseDetailNewDto.Prescription recommendPrescription = DiseaseDetailNewDto.Prescription.builder()
                    .name((String) exp.get("prescriptionName"))
                    .base(herbItems)
                    .build();
                
                // 加减法说明
                String modification = (String) exp.get("modification");
                List<String> additional = new ArrayList<>();
                if (modification != null && !modification.isEmpty()) {
                    String[] parts = modification.split("[。;；]");
                    for (String part : parts) {
                        String trimmed = part.trim();
                        if (!trimmed.isEmpty()) {
                            additional.add(trimmed);
                        }
                    }
                }
                
                return DiseaseDetailNewDto.ExpItem.builder()
                    .id(expId)
                    .name((String) exp.get("expertName"))
                    .authorIntro((String) exp.get("introduction"))
                    .experience((String) exp.get("prescriptionName"))
                    .explain((String) exp.get("composition"))
                    .majorFunction((String) exp.get("analysis"))
                    .spec(null)
                    .solution(null)
                    .preExplain(null)
                    .recommend(recommendPrescription)
                    .additional(additional)
                    .build();
            })
            .collect(Collectors.toList());

        DiseaseDetailNewDto.ExpData expData = DiseaseDetailNewDto.ExpData.builder()
            .data(expItems)
            .build();

        // 5. 组装详细数据
        DiseaseDetailNewDto.DetailData detailData = DiseaseDetailNewDto.DetailData.builder()
            .cure(cureData)
            .exp(expData)
            .build();

        builder.data(detailData);

        return builder.build();
    }
}
