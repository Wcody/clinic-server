/*
 * 版权声明 Copyright (c) 2026。
 * 版权所有者： [九维无纸化病案管理系统]
 */
package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.dtos.BqPrescriptionFullDto;
import com.qkplm.clinic.clinicserver.entity.*;
import com.qkplm.clinic.clinicserver.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.convert.out.fo.renderers.FORendererApacheFOP;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.apache.fop.apps.FopFactory;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType;

import java.math.BigInteger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 处方PDF生成服务：加载 template.docx → 填充数据 → 转换为PDF → 合并
 */
@Service
@RequiredArgsConstructor
public class BqPrescriptionPrintService {

    private final IBqPrescriptionService prescriptionService;
    private final IBqPatientService patientService;
    private final IBqRegistrationService registrationService;
    private final IBqMedicalRecordService medicalRecordService;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    // ==================== 公开入口 ====================

    public byte[] generatePdf(Integer regId, Boolean showPrice) throws Exception {
        List<BqPrescriptionFullDto> fullDtos = prescriptionService.listByRegId(regId);
        if (fullDtos.isEmpty()) {
            throw new RuntimeException("该挂号暂无处方信息");
        }

        BqRegistrationEntity reg = registrationService.getById(regId);
        BqPatientEntity patient = (reg != null && reg.getPatientId() != null)
                ? patientService.getById(reg.getPatientId())
                : null;

        // 获取诊断文本
        String diagnosis = "";
        Integer recordId = fullDtos.get(0).getPrescription().getRecordId();
        if (recordId != null) {
            BqMedicalRecordEntity record = medicalRecordService.getById(recordId);
            if (record != null && record.getDiagnosis() != null) {
                diagnosis = record.getDiagnosis();
            }
        }

        byte[] templateBytes = new ClassPathResource("templates/template.docx")
                .getInputStream().readAllBytes();

        if (fullDtos.size() == 1) {
            return fillAndConvert(templateBytes, reg, patient, fullDtos.get(0), diagnosis, showPrice);
        }

        // 多处方组 → 逐页生成PDF后合并
        PDFMergerUtility merger = new PDFMergerUtility();
        ByteArrayOutputStream mergedOut = new ByteArrayOutputStream();
        merger.setDestinationStream(mergedOut);
        for (BqPrescriptionFullDto dto : fullDtos) {
            byte[] pdfPage = fillAndConvert(templateBytes, reg, patient, dto, diagnosis, showPrice);
            merger.addSource(new RandomAccessReadBuffer(pdfPage));
        }
        merger.mergeDocuments(null);
        return mergedOut.toByteArray();
    }

    // ==================== 填充 + 转换 ====================

    private byte[] fillAndConvert(byte[] templateBytes, BqRegistrationEntity reg,
            BqPatientEntity patient, BqPrescriptionFullDto dto,
            String diagnosis, Boolean showPrice) throws Exception {
        byte[] filledDocx = fillTemplate(templateBytes, reg, patient, dto, diagnosis, showPrice);
        return toPdf(filledDocx);
    }

    private byte[] fillTemplate(byte[] templateBytes, BqRegistrationEntity reg,
            BqPatientEntity patient, BqPrescriptionFullDto dto,
            String diagnosis, Boolean showPrice) throws IOException {
        BqPrescriptionEntity presc = dto.getPrescription();
        List<BqPrescriptionItemEntity> items = dto.getItems();

        // 构建变量映射（对应模板占位符 {{key}}）
        Map<String, String> vars = new LinkedHashMap<>();
        vars.put("title", "茂名市高州市石鼓镇九罡村曾俊华卫生室");
        vars.put("a", presc.getPrescNo() != null ? presc.getPrescNo() : "R" + presc.getId());
        vars.put("b", reg != null ? str(reg.getPatient()) : "");
        vars.put("c", reg != null ? str(reg.getGender()) : "");
        vars.put("d", buildAge(reg));
        vars.put("e", ""); // 体重（暂无数据）
        vars.put("f", buildAllergyText(patient));
        vars.put("g", reg != null && reg.getOrderTime() != null
                ? reg.getOrderTime().format(DATE_FMT)
                : "");
        vars.put("diagnosis", diagnosis);
        vars.put("content", prescTypeName(presc.getPrescType()));
        vars.put("doctor", reg != null ? str(reg.getDoctor()) : "");
        vars.put("cost", Boolean.TRUE.equals(showPrice) && presc.getTotalPrice() != null
                ? presc.getTotalPrice().toPlainString()
                : "");

        // 构建数据：drug+sig交替配对，补足22行
        List<String> itemLines = buildItemLines(items, showPrice);
        List<String> sigLines = Collections.emptyList();

        // 填充数据
        try (InputStream is = new ByteArrayInputStream(templateBytes);
                XWPFDocument doc = new XWPFDocument(is)) {

            // 处理文档段落
            for (XWPFParagraph p : doc.getParagraphs()) {
                processParagraph(p, vars, itemLines, sigLines);
            }
            // 处理表格单元格
            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            processParagraph(p, vars, itemLines, sigLines);
                        }
                    }
                }
            }

            // 页边距设为0
            CTSectPr sectPr = doc.getDocument().getBody().getSectPr();
            if (sectPr == null)
                sectPr = doc.getDocument().getBody().addNewSectPr();
            CTPageMar pageMar = sectPr.isSetPgMar() ? sectPr.getPgMar() : sectPr.addNewPgMar();
            pageMar.setTop(BigInteger.ZERO);
            pageMar.setBottom(BigInteger.ZERO);
            pageMar.setLeft(BigInteger.ZERO);
            pageMar.setRight(BigInteger.ZERO);
            pageMar.setHeader(BigInteger.ZERO);
            pageMar.setFooter(BigInteger.ZERO);
            pageMar.setGutter(BigInteger.ZERO);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.write(out);
            return out.toByteArray();
        }
    }

    // ==================== 段落处理 ====================

    private void processParagraph(XWPFParagraph para, Map<String, String> vars,
            List<String> itemLines, List<String> sigLines) {
        List<XWPFRun> runs = para.getRuns();
        if (runs.isEmpty())
            return;

        // 将所有 run 文本拼接成完整段落文本
        StringBuilder sb = new StringBuilder();
        for (XWPFRun r : runs) {
            sb.append(r.getText(0) != null ? r.getText(0) : "");
        }
        String fullText = sb.toString();

        // 多行占位符：[drug] / [sig]
        if (fullText.contains("[drug]")) {
            applyMultiLine(runs, itemLines);
            return;
        }
        if (fullText.contains("[sig]")) {
            applyMultiLine(runs, sigLines);
            return;
        }

        // 单值占位符 {{key}}
        boolean changed = false;
        for (Map.Entry<String, String> entry : vars.entrySet()) {
            String pattern = "{{" + entry.getKey() + "}}";
            if (fullText.contains(pattern)) {
                fullText = fullText.replace(pattern, entry.getValue() != null ? entry.getValue() : "");
                changed = true;
            }
        }
        if (!changed)
            return;

        // 将替换结果写回第一个 run，其余 run 清空
        runs.get(0).setText(fullText, 0);
        for (int i = 1; i < runs.size(); i++) {
            runs.get(i).setText("", 0);
        }
    }

    /**
     * 将多行文本写入段落：第一个 run 承载全部行，行间插入软换行 &lt;w:br/&gt;。
     * 其余 run 清空文本，保留格式。
     */
    private void applyMultiLine(List<XWPFRun> runs, List<String> lines) {
        // 清空所有 run 文本
        for (XWPFRun r : runs) {
            CTR ctr = r.getCTR();
            for (int i = ctr.sizeOfTArray() - 1; i >= 0; i--)
                ctr.removeT(i);
            for (int i = ctr.sizeOfBrArray() - 1; i >= 0; i--)
                ctr.removeBr(i);
        }
        if (lines.isEmpty())
            return;

        // 在第一个 run 中依次追加 <w:t> 和 <w:br/>
        CTR firstCtr = runs.get(0).getCTR();
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) {
                CTBr br = firstCtr.addNewBr();
                br.setType(STBrType.TEXT_WRAPPING);
            }
            CTText t = firstCtr.addNewT();
            String content = lines.get(i);
            t.setStringValue(content.isEmpty() ? "\u00A0" : content);
        }
    }

    // ==================== PDF 转换 ====================

    private byte[] toPdf(byte[] docxBytes) throws Exception {
        try (InputStream is = new ByteArrayInputStream(docxBytes)) {
            WordprocessingMLPackage pkg = WordprocessingMLPackage.load(is);
            FOSettings fo = Docx4J.createFOSettings();
            fo.setOpcPackage(pkg);
            // // 直接用枚举设置 0 边距
            // pkg.getDocumentModel().getSections().forEach(section -> {
            // section.getPageDimensions().setMargins(
            // org.docx4j.model.structure.MarginsWellKnown.NARROW);
            // });
            FopFactory fopFactory = FORendererApacheFOP.getFopFactoryBuilder(fo).build();
            FORendererApacheFOP.getFOUserAgent(fo, fopFactory);
            ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
            Docx4J.toFO(fo, pdfOut, Docx4J.FLAG_EXPORT_PREFER_XSL);
            return pdfOut.toByteArray();
        }
    }

    // ==================== 数据构建辅助 ====================

    private String buildAge(BqRegistrationEntity reg) {
        if (reg == null)
            return "";
        if (reg.getAge() != null && !reg.getAge().isEmpty())
            return reg.getAge();
        if (reg.getFirstAge() != null) {
            String unit = reg.getAgeType() != null ? switch (reg.getAgeType()) {
                case 2 -> "月";
                case 3 -> "天";
                default -> "岁";
            } : "岁";
            return reg.getFirstAge() + unit;
        }
        return "";
    }

    private String buildAllergyText(BqPatientEntity patient) {
        if (patient == null)
            return "无";
        String history = patient.getAllergicHistory();
        return (history != null && !history.isBlank()) ? history : "无";
    }

    private String prescTypeName(Byte prescType) {
        if (prescType == null)
            return "";
        return switch (prescType) {
            case 1 -> "西  药  处  方";
            case 2 -> "中  药  处  方";
            case 3 -> "检  查  单";
            case 4 -> "处  置  单";
            default -> "处  方";
        };
    }

    // 每个药品输出 drug行+sig行，共22行，不足补空行
    private static final int MAX_ITEM_LINES = 30;

    private List<String> buildItemLines(List<BqPrescriptionItemEntity> items, Boolean showPrice) {
        List<String> lines = new ArrayList<>();
        for (BqPrescriptionItemEntity item : items) {
            // drug行
            StringBuilder drug = new StringBuilder(str(item.getItemName()));
            if (item.getSpec() != null && !item.getSpec().isEmpty())
                drug.append("  ").append(item.getSpec());
            if (item.getTotalNum() != null)
                drug.append("  ×").append(item.getTotalNum().stripTrailingZeros().toPlainString());
            if (Boolean.TRUE.equals(showPrice) && item.getTotalPrice() != null)
                drug.append("  ¥").append(item.getTotalPrice().toPlainString());
            lines.add(drug.toString());

            // sig行（仅药品类型=1）
            if (Byte.valueOf((byte) 1).equals(item.getItemType())) {
                StringBuilder sig = new StringBuilder("\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0用法：");
                if (item.getUseWay() != null)
                    sig.append(item.getUseWay()).append("  ");
                if (item.getSingleDosage() != null)
                    sig.append("每次").append(item.getSingleDosage()).append("  ").append(str(item.getUnit()))
                            .append("，");
                if (item.getFrequency() != null)
                    sig.append(item.getFrequency()).append("  ");
                if (item.getDays() != null)
                    sig.append("共").append(item.getDays()).append("天");
                if (item.getEntrust() != null && !item.getEntrust().isEmpty())
                    sig.append("（").append(item.getEntrust()).append("）");
                lines.add(sig.toString());
            } else {
                lines.add("");
            }
        }
        // 补足22行
        while (lines.size() < MAX_ITEM_LINES) {
            lines.add("");
        }
        return lines;
    }

    private String str(String s) {
        return s != null ? s : "";
    }
}