/*
 * 版权声明 Copyright (c) 2026。
 * 版权所有者： [全科医生系统]
 * 首创日期： 2026年4月19日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.qkplm.clinic.clinicserver.mapper.SysTcmDeptMapper;
import com.qkplm.clinic.clinicserver.mapper.SysTcmDiseaseMapper;
import com.qkplm.clinic.clinicserver.mapper.SysTcmExpertPrescriptionMapper;
import com.qkplm.clinic.clinicserver.mapper.SysTcmPrescriptionMapper;
import com.qkplm.clinic.clinicserver.mapper.SysTcmSyndromeMapper;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Wcke
 * @description <p>中医知识库 前端控制器</p>
 * @datetime 2026-4-19
 */
@RestController
@RequestMapping("/ams/api/v1/sys/tcm/dict")
public class SysTcmDictController {

    private static final String MODULE_NAME = "中医知识库";
    private static final String TAG_NAME = "sysTcmDict";

    private final SysTcmDiseaseMapper diseaseMapper;
    private final SysTcmDeptMapper deptMapper;
    private final SysTcmSyndromeMapper syndromeMapper;
    private final SysTcmPrescriptionMapper prescriptionMapper;
    private final SysTcmExpertPrescriptionMapper expertPrescriptionMapper;

    public SysTcmDictController(
            SysTcmDiseaseMapper diseaseMapper,
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

    /**
     * 获取疾病字典列表（疾病、首字母、科室）
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "获取疾病字典")
    @RequestMapping(value = "/getDictList", method = RequestMethod.GET)
    public Map<String, Object> getDictList() {
        // 疾病列表
        List<Map<String, Object>> rawDiseases = diseaseMapper.selectAllDiseases();
        List<Map<String, Object>> diseases = rawDiseases.stream().map(d -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", d.get("id"));
            m.put("name", d.get("diseaseName") != null ? d.get("diseaseName") : d.get("name"));
            m.put("category", d.get("category"));
            m.put("alphabet", d.get("alphabet"));
            m.put("pinyin", d.get("pinyin"));
            m.put("pinyinInitial", d.get("pinyinInitial"));
            return m;
        }).collect(Collectors.toList());

        // 科室列表
        List<Map<String, Object>> rawDepts = deptMapper.selectAllDepts();
        List<Map<String, Object>> categories = rawDepts.stream().map(d -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", d.get("id"));
            m.put("name", d.get("deptName") != null ? d.get("deptName") : d.get("name"));
            return m;
        }).collect(Collectors.toList());

        // 首字母列表（从疾病中提取，去重排序）
        List<Map<String, Object>> alphabets = diseases.stream()
                .map(d -> String.valueOf(d.get("alphabet")))
                .filter(a -> a != null && !a.isEmpty() && !"null".equals(a))
                .distinct()
                .sorted()
                .map(a -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", a);
                    m.put("name", a);
                    return m;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("diseases", diseases);
        result.put("alphabets", alphabets);
        result.put("categories", categories);
        return result;
    }

    /**
     * 获取疾病详情（概述、辨证治疗、名医经验方）
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "获取疾病详情")
    @RequestMapping(value = "/getDetail/{id}", method = RequestMethod.GET)
    public Map<String, Object> getDetail(@PathVariable Long id) {
        Map<String, Object> disease = diseaseMapper.selectById(id);
        if (disease == null) {
            throw new BQApiException("疾病不存在");
        }

        // 辨证治疗
        List<Map<String, Object>> rawSyndromes = syndromeMapper.selectByDiseaseId(id);
        List<Map<String, Object>> treatments = rawSyndromes.stream().map(s -> {
            Map<String, Object> t = new LinkedHashMap<>();
            t.put("id", s.get("id"));
            t.put("name", s.get("syndromeName"));
            t.put("spec", s.get("syndromeFeature"));
            t.put("solution", s.get("treatmentMethod"));

            Long syndromeId = toLong(s.get("id"));
            List<Map<String, Object>> rawPrescs = prescriptionMapper.selectBySyndromeId(syndromeId);
            if (!rawPrescs.isEmpty()) {
                Map<String, Object> rawPresc = rawPrescs.get(0);
                Long basicPrescId = toLong(rawPresc.get("basicPrescId"));
                List<Map<String, Object>> rawIngredients =
                        prescriptionMapper.selectIngredientsByPrescId(basicPrescId);

                List<Map<String, Object>> base = rawIngredients.stream().map(ing -> {
                    Map<String, Object> i = new LinkedHashMap<>();
                    i.put("name", ing.get("herbName"));
                    Object dosage = ing.get("dosage");
                    i.put("amount", dosage);
                    i.put("unit", "g");
                    i.put("remark", buildRemark(dosage, (String) ing.get("note")));
                    String note = (String) ing.get("note");
                    if (note != null && !note.isEmpty()) {
                        i.put("cook", note);
                    }
                    i.put("prescriptionType", 0);
                    return i;
                }).collect(Collectors.toList());

                Map<String, Object> recommend = new LinkedHashMap<>();
                recommend.put("name", rawPresc.get("prescName"));
                recommend.put("base", base);
                t.put("recommend", recommend);

                // 加减法
                String adjustment = (String) rawPresc.get("adjustment");
                t.put("additional", parseAdditional(adjustment));
            } else {
                t.put("recommend", null);
                t.put("additional", Collections.emptyList());
            }
            return t;
        }).collect(Collectors.toList());

        // 名医经验方
        List<Map<String, Object>> rawExperts = expertPrescriptionMapper.selectByDiseaseId(id);
        List<Map<String, Object>> experts = rawExperts.stream().map(e -> {
            Map<String, Object> exp = new LinkedHashMap<>();
            exp.put("id", e.get("id"));
            exp.put("name", e.get("expertName"));
            exp.put("authorIntro", e.get("introduction"));
            exp.put("experience", e.get("prescriptionName"));
            exp.put("explain", e.get("composition"));
            exp.put("majorFunction", e.get("analysis"));
            exp.put("spec", e.get("prescriptionName"));
            exp.put("solution", "");
            exp.put("preExplain", "");

            Long expertPrescId = toLong(e.get("id"));
            List<Map<String, Object>> rawItems =
                    expertPrescriptionMapper.selectItemsByExpertPrescId(expertPrescId);

            List<Map<String, Object>> base = rawItems.stream().map(item -> {
                Map<String, Object> i = new LinkedHashMap<>();
                i.put("name", item.get("herbName"));
                Object dosage = item.get("dosage");
                i.put("amount", dosage);
                i.put("unit", "g");
                i.put("remark", buildRemark(dosage, (String) item.get("note")));
                String note = (String) item.get("note");
                if (note != null && !note.isEmpty()) {
                    i.put("cook", note);
                }
                i.put("prescriptionType", 0);
                return i;
            }).collect(Collectors.toList());

            Map<String, Object> recommend = new LinkedHashMap<>();
            recommend.put("name", e.get("prescriptionName"));
            recommend.put("base", base);
            exp.put("recommend", recommend);

            exp.put("additional", parseAdditional((String) e.get("modification")));
            return exp;
        }).collect(Collectors.toList());

        // 组装响应
        Map<String, Object> cure = new LinkedHashMap<>();
        cure.put("data", treatments);
        cure.put("desc", "");

        Map<String, Object> expData = new LinkedHashMap<>();
        expData.put("data", experts);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("cure", cure);
        data.put("exp", expData);

        Map<String, Object> result = new LinkedHashMap<>();
        String diseaseName = disease.get("diseaseName") != null
                ? (String) disease.get("diseaseName")
                : (String) disease.get("name");
        result.put("name", diseaseName);
        result.put("summary", disease.get("summary"));
        result.put("data", data);
        return result;
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof Number) return ((Number) value).longValue();
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String buildRemark(Object dosage, String cook) {
        String dosageStr = dosage != null ? dosage.toString().trim() : "";
        // 只有当 dosage 不含字母时才追加 "g"，避免 dosage 已含单位时出现 "10gg"
        String remark = (!dosageStr.isEmpty() && !dosageStr.matches(".*[a-zA-Z].*"))
                ? dosageStr + "g"
                : dosageStr;
        if (cook != null && !cook.isEmpty() && !remark.contains("(" + cook + ")")) {
            remark += "(" + cook + ")";
        }
        return remark;
    }

    private List<String> parseAdditional(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.singletonList(null);
        }
        return Arrays.stream(text.split("\n"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
