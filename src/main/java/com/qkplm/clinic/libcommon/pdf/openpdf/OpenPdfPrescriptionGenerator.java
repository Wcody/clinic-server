/*
 * 版权声明 Copyright (c) 2026。
 * 版权所有者： [九维无纸化病案管理系统]
 */
package com.qkplm.clinic.libcommon.pdf.openpdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.qkplm.clinic.libcommon.pdf.PrescriptionData;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * 处方PDF生成器 - OpenPDF高速版本
 * <p>
 * 优势：直接生成PDF，无需DOCX中间转换，速度比Apache FOP快5-10倍
 */
public class OpenPdfPrescriptionGenerator {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    // 中文字体（系统内置）
    private static final BaseFont BASE_FONT;
    private static final BaseFont BASE_FONT_BOLD;

    static {
        try {
            // 使用系统中文宋体
            BASE_FONT = BaseFont.createFont("C:/Windows/Fonts/simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            BASE_FONT_BOLD = BaseFont.createFont("C:/Windows/Fonts/simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            throw new RuntimeException("无法加载中文字体", e);
        }
    }

    private static Font titleFont(int size) {
        return new Font(BASE_FONT_BOLD, size, Font.BOLD);
    }

    private static Font normalFont(int size) {
        return new Font(BASE_FONT, size, Font.NORMAL);
    }

    private static Font boldFont(int size) {
        return new Font(BASE_FONT_BOLD, size, Font.BOLD);
    }

    // ==================== 公开入口 ====================

    /**
     * 生成处方PDF字节数组
     *
     * @param datas     处方数据列表
     * @param showPrice 是否显示价格
     * @return PDF 字节数组
     */
    public static byte[] generatePdf(List<PrescriptionData> datas, Boolean showPrice) {
        if (datas == null || datas.isEmpty()) {
            throw new RuntimeException("处方数据为空");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 50, 50);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            for (int i = 0; i < datas.size(); i++) {
                if (i > 0) {
                    document.newPage();
                }
                addPrescriptionPage(document, datas.get(i), showPrice);
            }

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("PDF生成失败", e);
        }

        return out.toByteArray();
    }

    private static void addPrescriptionPage(Document document, PrescriptionData data, Boolean showPrice)
            throws DocumentException {
        // 标题
        Paragraph title = new Paragraph("茂名市高州市石鼓镇九罡村曾俊华卫生室", titleFont(18));
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        // 处方类型
        Paragraph prescType = new Paragraph(prescTypeName(data.getPrescType()), titleFont(14));
        prescType.setAlignment(Element.ALIGN_CENTER);
        prescType.setSpacingAfter(15);
        document.add(prescType);

        // 基本信息区
        PdfPTable infoTable = new PdfPTable(4);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[]{2, 1.5f, 1, 1.5f});

        addInfoCell(infoTable, "处方号：" + nvl(data.getPrescNo() != null ? data.getPrescNo() : "R" + data.getPrescId()), 1);
        addInfoCell(infoTable, "姓名：" + nvl(data.getPatientName()), 1);
        addInfoCell(infoTable, "性别：" + nvl(data.getGender()), 1);
        addInfoCell(infoTable, "年龄：" + buildAge(data), 1);

        document.add(infoTable);

        PdfPTable infoTable2 = new PdfPTable(2);
        infoTable2.setWidthPercentage(100);
        infoTable2.setWidths(new float[]{1, 3});

        addInfoCell(infoTable2, "体重：" + "", 1);
        addInfoCell(infoTable2, "过敏史：" + buildAllergyText(data), 1);

        document.add(infoTable2);

        // 日期和诊断
        PdfPTable infoTable3 = new PdfPTable(2);
        infoTable3.setWidthPercentage(100);
        infoTable3.setWidths(new float[]{1, 3});

        addInfoCell(infoTable3, "日期：" + (data.getOrderTime() != null ? data.getOrderTime().format(DATE_FMT) : ""), 1);
        addInfoCell(infoTable3, "诊断：" + nvl(data.getDiagnosis()), 1);

        document.add(infoTable3);

        // 空行
        document.add(new Paragraph(" ", normalFont(8)));

        // 药品表格
        addItemTable(document, data, showPrice);

        // 空行
        document.add(new Paragraph(" ", normalFont(10)));

        // 底部签名区
        PdfPTable signTable = new PdfPTable(2);
        signTable.setWidthPercentage(100);
        signTable.setWidths(new float[]{1, 1});

        addInfoCell(signTable, "医生签名：" + nvl(data.getDoctor()), 1);
        if (Boolean.TRUE.equals(showPrice)) {
            addInfoCell(signTable, "合计金额：" + (data.getTotalPrice() != null ? "¥" + data.getTotalPrice().toPlainString() : ""), 1);
        } else {
            addInfoCell(signTable, "", 1);
        }

        document.add(signTable);
    }

    private static void addInfoCell(PdfPTable table, String text, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(text, normalFont(10)));
        cell.setColspan(colspan);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingTop(3);
        cell.setPaddingBottom(3);
        table.addCell(cell);
    }

    private static void addItemTable(Document document, PrescriptionData data, Boolean showPrice)
            throws DocumentException {
        // 表格列数：药品名、规格、数量、用法、价格（条件）
        int cols = 4;
        if (Boolean.TRUE.equals(showPrice)) {
            cols = 5;
        }

        float[] widths;
        if (cols == 5) {
            widths = new float[]{3, 2.5f, 1, 2, 1};
        } else {
            widths = new float[]{3, 2.5f, 1, 2};
        }

        PdfPTable table = new PdfPTable(cols);
        table.setWidthPercentage(100);
        table.setWidths(widths);

        // 表头
        addHeaderCell(table, "药品名称");
        addHeaderCell(table, "规格");
        addHeaderCell(table, "数量");
        addHeaderCell(table, "用法");
        if (Boolean.TRUE.equals(showPrice)) {
            addHeaderCell(table, "价格");
        }

        // 数据行
        List<PrescriptionData.PrescriptionItem> items = data.getItems();
        if (items == null) {
            items = Collections.emptyList();
        }

        for (int i = 0; i < Math.max(items.size(), 15); i++) {
            if (i < items.size()) {
                PrescriptionData.PrescriptionItem item = items.get(i);
                addItemRow(table, item, showPrice);
            } else {
                // 空行
                for (int j = 0; j < cols; j++) {
                    PdfPCell cell = new PdfPCell(new Phrase(" ", normalFont(9)));
                    cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    cell.setFixedHeight(22);
                    cell.setPaddingTop(2);
                    cell.setPaddingBottom(2);
                    table.addCell(cell);
                }
            }
        }

        document.add(table);
    }

    private static void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, boldFont(10)));
        cell.setBackgroundColor(new Color(240, 240, 240));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        cell.setBorder(Rectangle.BOX);
        cell.setBorderColor(Color.GRAY);
        table.addCell(cell);
    }

    private static void addItemRow(PdfPTable table, PrescriptionData.PrescriptionItem item, Boolean showPrice) {
        // 药品名
        PdfPCell nameCell = new PdfPCell(new Phrase(nvl(item.getItemName()), normalFont(9)));
        nameCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        nameCell.setPaddingTop(3);
        nameCell.setPaddingBottom(3);
        table.addCell(nameCell);

        // 规格
        PdfPCell specCell = new PdfPCell(new Phrase(nvl(item.getSpec()), normalFont(9)));
        specCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        specCell.setPaddingTop(3);
        specCell.setPaddingBottom(3);
        table.addCell(specCell);

        // 数量
        String numStr = "";
        if (item.getTotalNum() != null) {
            numStr = item.getTotalNum().stripTrailingZeros().toPlainString();
        }
        PdfPCell numCell = new PdfPCell(new Phrase(numStr, normalFont(9)));
        numCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        numCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        numCell.setPaddingTop(3);
        numCell.setPaddingBottom(3);
        table.addCell(numCell);

        // 用法（西药显示用法详情，其他显示空或 entrust）
        String usage = buildUsage(item);
        PdfPCell usageCell = new PdfPCell(new Phrase(usage, normalFont(8)));
        usageCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        usageCell.setPaddingTop(3);
        usageCell.setPaddingBottom(3);
        table.addCell(usageCell);

        // 价格
        if (Boolean.TRUE.equals(showPrice)) {
            String priceStr = "";
            if (item.getTotalPrice() != null) {
                priceStr = "¥" + item.getTotalPrice().toPlainString();
            }
            PdfPCell priceCell = new PdfPCell(new Phrase(priceStr, normalFont(9)));
            priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            priceCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
            priceCell.setPaddingTop(3);
            priceCell.setPaddingBottom(3);
            table.addCell(priceCell);
        }
    }

    private static String buildUsage(PrescriptionData.PrescriptionItem item) {
        if (item == null) return "";

        // itemType=1 是西药，显示完整用法
        if (Byte.valueOf((byte) 1).equals(item.getItemType())) {
            StringBuilder sb = new StringBuilder();
            if (item.getUseWay() != null) sb.append(item.getUseWay());
            if (item.getSingleDosage() != null && item.getUnit() != null) {
                sb.append(" 每次").append(item.getSingleDosage()).append(item.getUnit());
            }
            if (item.getFrequency() != null) sb.append(" ").append(item.getFrequency());
            if (item.getDays() != null) sb.append(" 共").append(item.getDays()).append("天");
            if (item.getEntrust() != null && !item.getEntrust().isEmpty()) {
                sb.append("（").append(item.getEntrust()).append("）");
            }
            return sb.toString();
        }

        // 其他类型显示 entrust
        return item.getEntrust() != null ? item.getEntrust() : "";
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
            return "处  方";
        return switch (prescType) {
            case 1 -> "西  药  处  方";
            case 2 -> "中  药  处  方";
            case 3 -> "检  查  单";
            case 4 -> "处  置  单";
            default -> "处  方";
        };
    }

    private static String nvl(String s) {
        return s != null ? s : "";
    }

    // ==================== 本地调试 main ====================

    public static void main(String[] args) throws Exception {
        System.out.println("========== OpenPDF处方PDF生成器 - 本地调试 ==========");

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
        data.setOrderTime(LocalDateTime.now());
        data.setDoctor("李医生");
        data.setAllergicHistory("青霉素过敏");
        data.setDiagnosis("急性上呼吸道感染");
        data.setPrescNo("R202604220001");
        data.setPrescId(1);
        data.setPrescType((byte) 1);
        data.setTotalPrice(new java.math.BigDecimal("69.50"));
        data.setItems(List.of(item1, item2, item3));

        // 多处方测试
        PrescriptionData data2 = new PrescriptionData();
        data2.setPatientName("李四");
        data2.setGender("女");
        data2.setAge("28岁");
        data2.setAgeType("1");
        data2.setOrderTime(LocalDateTime.now());
        data2.setDoctor("王医生");
        data2.setAllergicHistory("无");
        data2.setDiagnosis("急性胃肠炎");
        data2.setPrescNo("R202604220002");
        data2.setPrescId(2);
        data2.setPrescType((byte) 1);
        data2.setTotalPrice(new java.math.BigDecimal("45.00"));
        data2.setItems(List.of(
            createSimpleItem("蒙脱石散", "3g×10袋", "3", "口服", "tid", 3, "¥28.50"),
            createSimpleItem("口服补液盐III", "5.125g×6袋", "2", "口服", "tid", 3, "¥16.50")
        ));

        // ============================================

        long start = System.currentTimeMillis();

        // 生成单处方PDF
        byte[] pdfBytes = generatePdf(List.of(data), true);

        // 生成多处方PDF（测试合并）
        // byte[] pdfBytes = generatePdf(List.of(data, data2), true);

        long end = System.currentTimeMillis();

        // 输出到文件
        String baseDir = System.getProperty("user.dir");
        String outputPath = Paths.get(baseDir, "src/main/resources/fonts/openpdf_output.pdf").toString();

        // 确保目录存在
        Files.createDirectories(Paths.get(baseDir, "src/main/resources/fonts"));

        Files.write(Paths.get(outputPath), pdfBytes);

        System.out.println("PDF 生成成功: " + outputPath);
        System.out.println("文件大小: " + pdfBytes.length + " bytes");
        System.out.println("耗时: " + (end - start) + " ms");
        System.out.println("========== 调试结束 ==========");
    }

    private static PrescriptionData.PrescriptionItem createSimpleItem(
            String name, String spec, String num,
            String useWay, String frequency, int days, String price) {
        PrescriptionData.PrescriptionItem item = new PrescriptionData.PrescriptionItem();
        item.setItemType((byte) 1);
        item.setItemName(name);
        item.setSpec(spec);
        item.setTotalNum(new java.math.BigDecimal(num));
        item.setTotalPrice(new java.math.BigDecimal(price.replace("¥", "")));
        item.setSingleDosage(num);
        item.setUnit("袋");
        item.setUseWay(useWay);
        item.setFrequency(frequency);
        item.setDays(days);
        return item;
    }
}