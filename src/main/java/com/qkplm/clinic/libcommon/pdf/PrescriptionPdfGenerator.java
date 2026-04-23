/*
 * 版权声明 Copyright (c) 2026。
 * 版权所有者： [九维无纸化病案管理系统]
 */
package com.qkplm.clinic.libcommon.pdf;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.convert.out.fo.renderers.FORendererApacheFOP;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 处方PDF生成器（纯本地调试版，不依赖Spring/MyBatis）
 * <p>
 * 用法：直接运行 main 方法，根据需要修改其中的 mock 数据即可本地调试
 */
public class PrescriptionPdfGenerator {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    // ==================== 公开入口 ====================

    /**
     * 生成处方PDF字节数组
     *
     * @param datas         处方数据列表
     * @param showPrice     是否显示价格
     * @param templateBytes docx模板字节数组（从 resources 加载）
     * @return PDF 字节数组
     */
    public static byte[] generatePdf(List<PrescriptionData> datas, Boolean showPrice, byte[] templateBytes)
            throws Exception {
        if (datas == null || datas.isEmpty()) {
            throw new RuntimeException("处方数据为空");
        }

        if (datas.size() == 1) {
            return fillAndConvert(templateBytes, datas.get(0), showPrice);
        }

        // 多处方组 → 逐页生成PDF后合并
        PDFMergerUtility merger = new PDFMergerUtility();
        ByteArrayOutputStream mergedOut = new ByteArrayOutputStream();
        merger.setDestinationStream(mergedOut);
        for (PrescriptionData data : datas) {
            byte[] pdfPage = fillAndConvert(templateBytes, data, showPrice);
            merger.addSource(new RandomAccessReadBuffer(pdfPage));
        }
        merger.mergeDocuments(null);
        return mergedOut.toByteArray();
    }

    // ==================== 填充 + 转换 ====================

    private static byte[] fillAndConvert(byte[] templateBytes, PrescriptionData data, Boolean showPrice)
            throws Exception {
        byte[] filledDocx = fillTemplate(templateBytes, data, showPrice);
        return toPdf(filledDocx);
    }

    private static byte[] fillTemplate(byte[] templateBytes, PrescriptionData data, Boolean showPrice)
            throws IOException {
        // 构建变量映射（对应模板占位符 {{key}}）
        Map<String, String> vars = new LinkedHashMap<>();
        vars.put("title", "茂名市高州市石鼓镇九罡村曾俊华卫生室");
        vars.put("a", data.getPrescNo() != null ? data.getPrescNo() : "R" + data.getPrescId());
        vars.put("b", data.getPatientName() != null ? data.getPatientName() : "");
        vars.put("c", data.getGender() != null ? data.getGender() : "");
        vars.put("d", buildAge(data));
        vars.put("e", ""); // 体重（暂无数据）
        vars.put("f", buildAllergyText(data));
        vars.put("g", data.getOrderTime() != null ? data.getOrderTime().format(DATE_FMT) : "");
        vars.put("diagnosis", data.getDiagnosis() != null ? data.getDiagnosis() : "");
        vars.put("content", prescTypeName(data.getPrescType()));
        vars.put("doctor", data.getDoctor() != null ? data.getDoctor() : "");
        vars.put("cost", Boolean.TRUE.equals(showPrice) && data.getTotalPrice() != null
                ? data.getTotalPrice().toPlainString()
                : "");

        // 构建药品行列表
        List<String> itemLines = buildItemLines(data.getItems(), showPrice);
        List<String> sigLines = Collections.emptyList();

        // 填充数据
        try (InputStream is = new ByteArrayInputStream(templateBytes);
                XWPFDocument doc = new XWPFDocument(is)) {

            for (XWPFParagraph p : doc.getParagraphs()) {
                processParagraph(p, vars, itemLines, sigLines);
            }
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

    private static void processParagraph(XWPFParagraph para, Map<String, String> vars,
            List<String> itemLines, List<String> sigLines) {
        List<XWPFRun> runs = para.getRuns();
        if (runs.isEmpty())
            return;

        StringBuilder sb = new StringBuilder();
        for (XWPFRun r : runs) {
            sb.append(r.getText(0) != null ? r.getText(0) : "");
        }
        String fullText = sb.toString();

        if (fullText.contains("[drug]")) {
            applyMultiLine(runs, itemLines);
            return;
        }
        if (fullText.contains("[sig]")) {
            applyMultiLine(runs, sigLines);
            return;
        }

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

        runs.get(0).setText(fullText, 0);
        for (int i = 1; i < runs.size(); i++) {
            runs.get(i).setText("", 0);
        }
    }

    private static void applyMultiLine(List<XWPFRun> runs, List<String> lines) {
        for (XWPFRun r : runs) {
            CTR ctr = r.getCTR();
            for (int i = ctr.sizeOfTArray() - 1; i >= 0; i--)
                ctr.removeT(i);
            for (int i = ctr.sizeOfBrArray() - 1; i >= 0; i--)
                ctr.removeBr(i);
        }
        if (lines.isEmpty())
            return;

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

    private static byte[] toPdf(byte[] docxBytes) throws Exception {
        try (InputStream is = new ByteArrayInputStream(docxBytes)) {
            WordprocessingMLPackage pkg = WordprocessingMLPackage.load(is);
            FOSettings fo = Docx4J.createFOSettings();
            fo.setOpcPackage(pkg);
            // 通过 FopConfParser 加载字体配置
            // 注意：运行 main 时工作目录是项目根目录，因此用相对路径
            String baseDir = System.getProperty("user.dir");
            File fopConfigFile = Paths.get(baseDir, "src/main/resources/fop-userconfig.xml").toFile();
            if (!fopConfigFile.exists()) {
                throw new RuntimeException("fop-userconfig.xml not found at: " + fopConfigFile.getAbsolutePath());
            }
            org.apache.fop.apps.FopConfParser parser = new org.apache.fop.apps.FopConfParser(fopConfigFile,
                    Paths.get(baseDir).toUri());
            org.apache.fop.apps.FopFactory fopFactory = parser.getFopFactoryBuilder().build();
            FORendererApacheFOP.getFOUserAgent(fo, fopFactory);
            ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
            Docx4J.toFO(fo, pdfOut, Docx4J.FLAG_EXPORT_PREFER_XSL);
            return pdfOut.toByteArray();
        }
    }

    // ==================== 数据构建辅助 ====================

    private static String buildAge(PrescriptionData data) {
        if (data.getAge() != null && !data.getAge().isEmpty())
            return data.getAge();
        if (data.getFirstAge() != null) {
            String unit = data.getAgeType() != null ? switch (data.getAgeType()) {
                case "2" -> "月";
                case "3" -> "天";
                default -> "岁";
            } : "岁";
            return data.getFirstAge() + unit;
        }
        return "";
    }

    private static String buildAllergyText(PrescriptionData data) {
        String history = data.getAllergicHistory();
        return (history != null && !history.isBlank()) ? history : "无";
    }

    private static String prescTypeName(Byte prescType) {
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

    private static final int MAX_ITEM_LINES = 30;

    private static List<String> buildItemLines(List<PrescriptionData.PrescriptionItem> items, Boolean showPrice) {
        List<String> lines = new ArrayList<>();
        if (items == null)
            items = Collections.emptyList();
        for (PrescriptionData.PrescriptionItem item : items) {
            StringBuilder drug = new StringBuilder(str(item.getItemName()));
            if (item.getSpec() != null && !item.getSpec().isEmpty())
                drug.append("  ").append(item.getSpec());
            if (item.getTotalNum() != null)
                drug.append("  ×").append(item.getTotalNum().stripTrailingZeros().toPlainString());
            if (Boolean.TRUE.equals(showPrice) && item.getTotalPrice() != null)
                drug.append("  ¥").append(item.getTotalPrice().toPlainString());
            lines.add(drug.toString());

            if (Byte.valueOf((byte) 1).equals(item.getItemType())) {
                StringBuilder sig = new StringBuilder("\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0用法：");
                if (item.getUseWay() != null)
                    sig.append(item.getUseWay()).append("  ");
                if (item.getSingleDosage() != null)
                    sig.append("每次").append(item.getSingleDosage()).append("  ")
                            .append(str(item.getUnit())).append("，");
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
        while (lines.size() < MAX_ITEM_LINES) {
            lines.add("");
        }
        return lines;
    }

    private static String str(String s) {
        return s != null ? s : "";
    }

    // ==================== 本地调试 main ====================

    public static void main(String[] args) throws Exception {
        System.out.println("========== 处方PDF生成器 - 本地调试 ==========");
        System.out.println("当前工作目录: " + System.getProperty("user.dir"));

        // 加载模板文件
        String baseDir = System.getProperty("user.dir");
        File templateFile = Paths.get(baseDir, "src/main/resources/templates/template1.docx").toFile();
        System.out.println("模板文件: " + templateFile.getAbsolutePath() + " -> " + (templateFile.exists() ? "存在" : "不存在"));
        byte[] templateBytes = Files.readAllBytes(templateFile.toPath());

        // ========== 修改这里的数据进行调试 ==========

        PrescriptionData.PrescriptionItem item1 = new PrescriptionData.PrescriptionItem();
        item1.setItemType((byte) 1);
        item1.setItemName("阿莫西林胶囊");
        item1.setSpec("0.5g×24粒");
        item1.setTotalNum(new java.math.BigDecimal("2"));
        item1.setTotalPrice(new java.math.BigDecimal("36.00"));
        item1.setSingleDosage("0.5");
        item1.setUnit("粒");
        item1.setUseWay("口服");
        item1.setFrequency("tid");
        item1.setDays(7);

        PrescriptionData.PrescriptionItem item2 = new PrescriptionData.PrescriptionItem();
        item2.setItemType((byte) 1);
        item2.setItemName("布洛芬片");
        item2.setSpec("0.2g×20片");
        item2.setTotalNum(new java.math.BigDecimal("1"));
        item2.setTotalPrice(new java.math.BigDecimal("8.50"));
        item2.setSingleDosage("0.2");
        item2.setUnit("片");
        item2.setUseWay("口服");
        item2.setFrequency("tid");
        item2.setDays(3);
        item2.setEntrust("发热时服用");

        PrescriptionData.PrescriptionItem item3 = new PrescriptionData.PrescriptionItem();
        item3.setItemType((byte) 101);
        item3.setItemName("血常规检查");
        item3.setSpec("");
        item3.setTotalNum(new java.math.BigDecimal("1"));
        item3.setTotalPrice(new java.math.BigDecimal("25.00"));

        PrescriptionData data = new PrescriptionData();
        data.setPatientName("张三");
        data.setGender("男");
        data.setAge("35岁");
        data.setAgeType("1");
        data.setOrderTime(java.time.LocalDateTime.now());
        data.setDoctor("李医生");
        data.setAllergicHistory("青霉素过敏");
        data.setDiagnosis("急性上呼吸道感染");
        data.setPrescNo("R202604220001");
        data.setPrescId(1);
        data.setPrescType((byte) 1);
        data.setTotalPrice(new java.math.BigDecimal("69.50"));
        data.setItems(List.of(item1, item2, item3));

        // ============================================

        List<PrescriptionData> datas = List.of(data);

        // 生成PDF
        byte[] pdfBytes = generatePdf(datas, true, templateBytes);

        // 输出到文件
        String outputPath = Paths.get(baseDir, "src/main/resources/fonts/debug_output.pdf").toString();
        Files.write(Paths.get(outputPath), pdfBytes);
        System.out.println("PDF 生成成功: " + outputPath + " (" + pdfBytes.length + " bytes)");
        System.out.println("========== 调试结束 ==========");
    }
}