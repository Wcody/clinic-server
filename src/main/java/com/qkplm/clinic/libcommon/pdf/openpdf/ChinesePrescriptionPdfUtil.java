/*
 * 版权声明 Copyright (c) 2026。
 */
package com.qkplm.clinic.libcommon.pdf.openpdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;

/**
 * 中药处方 · 最终完美版
 * 规则：
 * 1. 非最后一页 → 显示（以下空白）
 * 2. 最后一页 → 显示 总剂数、每日剂数、医嘱 + （以下空白）
 * 3. 每页都正常显示
 */
public class ChinesePrescriptionPdfUtil {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    private static final BaseFont BASE_FONT;
    private static final BaseFont BASE_FONT_BOLD;
    private static final float ROW_HEIGHT = 18f;
    private static final int TCM_ROW_PER_PAGE = 12;

    static {
        try {
            // ✅ 正确加载 SpringBoot resources 下的字体（100% 不报错）
            ClassPathResource regular = new ClassPathResource("fonts/AlibabaHealthFont2.0CN-45R.ttf");
            ClassPathResource bold = new ClassPathResource("fonts/AlibabaHealthFont2.0CN-85B.ttf");

            BASE_FONT = BaseFont.createFont(
                    regular.getPath(),
                    BaseFont.IDENTITY_H,
                    BaseFont.NOT_EMBEDDED);
            BASE_FONT_BOLD = BaseFont.createFont(
                    bold.getPath(),
                    BaseFont.IDENTITY_H,
                    BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            throw new RuntimeException("字体加载失败");
        }
    }

    private static Font titleFont(int size) {
        return new Font(BASE_FONT_BOLD, size);
    }

    private static Font normalFont(int size) {
        return new Font(BASE_FONT_BOLD, size);
    }

    private static Font boldFont(int size) {
        return new Font(BASE_FONT_BOLD, size);
    }

    @Data
    public static class PrescriptionDTO {
        private String prescNo;
        private String patientName;
        private String gender;
        private String age;
        private Integer firstAge;
        private String ageType;
        private String idCard;
        private String clinicNo;
        private String phone;
        private LocalDateTime orderTime;
        private String address;
        private String allergicHistory;
        private String diagnosis;
        private String treatmentSuggest;
        private String doctor;
        private java.math.BigDecimal totalPrice;

        private String totalDosage;
        private String dailyDosage;
        private String medicalAdvice;
        private List<PrescriptionItemDTO> items;
    }

    @Data
    public static class PrescriptionItemDTO {
        private Byte itemType;
        private String itemName;
        private String dosage;
        private String decoctWay;
    }

    public static byte[] generatePrescriptionPdf(List<PrescriptionDTO> prescriptionList, boolean showPrice) {
        if (prescriptionList == null || prescriptionList.isEmpty()) {
            throw new RuntimeException("处方数据不能为空");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A5, 25, 25, 15, 15);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            for (PrescriptionDTO dto : prescriptionList) {
                renderOnePrescription(document, writer, dto, showPrice);
            }
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF生成失败", e);
        }
    }

    private static void renderOnePrescription(Document document, PdfWriter writer, PrescriptionDTO dto,
            boolean showPrice) throws Exception {
        List<PrescriptionItemDTO> itemList = Optional.ofNullable(dto.getItems()).orElse(new ArrayList<>());
        int totalRow = (itemList.size() + 1) / 2;
        int currRow = 0;
        int pageNum = 0;

        while (currRow < totalRow) {
            pageNum++;
            if (pageNum > 1)
                document.newPage();
            addHeader(document, dto);

            int endRow = Math.min(currRow + TCM_ROW_PER_PAGE, totalRow);
            addTcmTable(document, itemList, currRow, endRow);
            currRow = endRow;

            boolean isLastPage = (currRow >= totalRow);

            // ========== 核心逻辑 ==========
            if (isLastPage) {
                // 最后一页：显示 剂数 + 医嘱 + 以下空白
                addTcmBottomRemark(document, dto);
            }

            int totalPage = (totalRow + TCM_ROW_PER_PAGE - 1) / TCM_ROW_PER_PAGE;
            addFooter(writer, dto, showPrice, pageNum, totalPage);
        }
    }

    private static void addEmptyTip(Document document) throws Exception {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(95);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(getLeftCell("（以下空白）"));
        document.add(table);
    }

    private static void addTcmBottomRemark(Document document, PrescriptionDTO dto) throws Exception {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(95);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(getLeftCell("总剂数：" + nvl(dto.getTotalDosage())));
        table.addCell(getLeftCell("用法：" + nvl(dto.getDailyDosage())));
        table.addCell(getLeftCell("医嘱：" + nvl(dto.getMedicalAdvice())));
        document.add(table);
    }

    private static void addTcmTable(Document document, List<PrescriptionItemDTO> allItems, int startRow, int endRow)
            throws Exception {
        // 2 列布局，每行 2 味药
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(95);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new float[] { 1, 1 });

        for (int row = startRow; row < endRow; row++) {
            int idx1 = row * 2;
            int idx2 = row * 2 + 1;

            String col1 = idx1 < allItems.size() ? formatTcmItem(allItems.get(idx1)) : "";
            String col2 = idx2 < allItems.size() ? formatTcmItem(allItems.get(idx2)) : "";

            table.addCell(getLeftCell(col1));
            table.addCell(getLeftCell(col2));
        }

        int emptyRow = TCM_ROW_PER_PAGE - (endRow - startRow);
        // 以下空白行
        table.addCell(getLeftCell("（以下空白）"));
        table.addCell(getLeftCell(""));
        emptyRow--;
        for (int i = 0; i < emptyRow; i++) {
            table.addCell(getLeftCell(""));
            table.addCell(getLeftCell(""));
        }

        document.add(table);
    }

    private static String formatTcmItem(PrescriptionItemDTO item) {
        StringBuilder sb = new StringBuilder();
        sb.append(nvl(item.getItemName())).append("   ").append(nvl(item.getDosage()));
        if (item.getDecoctWay() != null && !item.getDecoctWay().isEmpty()) {
            sb.append("（").append(item.getDecoctWay()).append("）");
        }
        return sb.toString();
    }

    private static PdfPCell getLeftCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, normalFont(10)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(ROW_HEIGHT);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingLeft(6);
        return cell;
    }

    private static void addHeader(Document document, PrescriptionDTO dto) throws Exception {
        PdfPTable tTitle = new PdfPTable(1);
        tTitle.setWidthPercentage(100);
        addCellCenter(tTitle, "茂名市高州市石鼓镇九罡村曾俊华卫生室", titleFont(17));
        document.add(tTitle);

        PdfPTable tSub = new PdfPTable(1);
        tSub.setWidthPercentage(100);
        addCellCenter(tSub, "处方笺", titleFont(16));
        document.add(tSub);

        PdfPTable t3 = new PdfPTable(2);
        t3.setWidthPercentage(100);
        t3.setWidths(new float[] { 1, 1 });
        addCellLeftBottom(t3, "费别：自费", boldFont(10));
        addCellRightBottom(t3, "处方编号：" + nvl(dto.getPrescNo()), boldFont(10));
        document.add(t3);

        PdfPTable t4 = new PdfPTable(3);
        t4.setWidthPercentage(100);
        t4.setWidths(new float[] { 1, 1, 1 });
        addCellLeft(t4, "姓名：" + nvl(dto.getPatientName()), normalFont(10));
        addCellCenter(t4, "性别：" + nvl(dto.getGender()), normalFont(10));
        addCellRight(t4, "年龄：" + getAgeText(dto), normalFont(10));
        document.add(t4);

        PdfPTable t5 = new PdfPTable(2);
        t5.setWidthPercentage(100);
        addCellLeft(t5, "身份证号：" + nvl(dto.getIdCard()), normalFont(10));
        addCellRight(t5, "门诊编号：" + nvl(dto.getClinicNo()), normalFont(10));
        document.add(t5);

        PdfPTable t6 = new PdfPTable(2);
        t6.setWidthPercentage(100);
        String date = dto.getOrderTime() != null ? dto.getOrderTime().format(DATE_FMT) : "";
        addCellLeft(t6, "联系电话：" + nvl(dto.getPhone()), normalFont(10));
        addCellRight(t6, "开具日期：" + date, normalFont(10));
        document.add(t6);

        PdfPTable tAddr = new PdfPTable(1);
        tAddr.setWidthPercentage(100);
        addCellLeft(tAddr, "地址：" + nvl(dto.getAddress()), normalFont(10));
        document.add(tAddr);

        PdfPTable tAllergy = new PdfPTable(1);
        tAllergy.setWidthPercentage(100);
        String allergy = (dto.getAllergicHistory() == null || dto.getAllergicHistory().isBlank()) ? "无"
                : dto.getAllergicHistory();
        addCellLeft(tAllergy, "过敏史：" + allergy, normalFont(10));
        document.add(tAllergy);

        PdfPTable tDiag = new PdfPTable(1);
        tDiag.setWidthPercentage(100);
        addCellLeft(tDiag, "临床诊断：" + nvl(dto.getDiagnosis()), normalFont(10));
        document.add(tDiag);

        PdfPTable tSuggest = new PdfPTable(1);
        tSuggest.setWidthPercentage(100);
        addCellLeft(tSuggest, "治疗建议：" + nvl(dto.getTreatmentSuggest()), normalFont(10));
        document.add(tSuggest);

        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100);
        PdfPCell cLine = new PdfPCell(new Phrase(""));
        cLine.setBorder(Rectangle.BOTTOM);
        cLine.setBorderWidth(0.5f);
        cLine.setPadding(1);
        line.addCell(cLine);
        document.add(line);

        PdfPTable rp = new PdfPTable(1);
        rp.setWidthPercentage(100);
        addCellLeft(rp, "RP", boldFont(14));
        document.add(rp);
    }

    private static void addFooter(PdfWriter writer, PrescriptionDTO dto, boolean showPrice, int currentPage,
            int totalPage) {
        PdfContentByte cb = writer.getDirectContent();
        Rectangle pageSize = writer.getPageSize();
        float left = 25f;
        float width = pageSize.getWidth() - 50;
        float y = pageSize.getBottom() + 40f;

        PdfPTable t1 = new PdfPTable(3);
        t1.setTotalWidth(width);
        t1.setLockedWidth(true);
        t1.setWidths(new float[] { 1, 1, 1 });
        addCellNoBorder(t1, "审核：", normalFont(9));
        addCellNoBorder(t1, "调配：", normalFont(9));
        addCellNoBorder(t1, "核对发药：", normalFont(9));
        t1.writeSelectedRows(0, -1, left, y, cb);
        y += t1.getTotalHeight() + 3;

        PdfPTable t2 = new PdfPTable(3);
        t2.setTotalWidth(width);
        t2.setLockedWidth(true);
        t2.setWidths(new float[] { 1, 1, 1 });
        String priceText = "";
        if (showPrice && dto.getTotalPrice() != null) {
            priceText = "¥" + dto.getTotalPrice().stripTrailingZeros().toPlainString();
        }
        addCellNoBorder(t2, "医生：" + nvl(dto.getDoctor()), normalFont(9));
        addCellNoBorder(t2, "总金额：" + priceText, boldFont(9));
        addCellNoBorder(t2, "医师签名：", normalFont(9));
        t2.writeSelectedRows(0, -1, left, y, cb);
        y += t2.getTotalHeight() + 11;

        PdfPTable t3 = new PdfPTable(1);
        t3.setTotalWidth(width);
        t3.setLockedWidth(true);
        PdfPCell pageCell = new PdfPCell(new Phrase("第" + currentPage + "页，共" + totalPage + "页", normalFont(9)));
        pageCell.setBorder(Rectangle.BOTTOM);
        pageCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pageCell.setPadding(4);
        t3.addCell(pageCell);
        t3.writeSelectedRows(0, -1, left, y, cb);
    }

    private static String getAgeText(PrescriptionDTO dto) {
        if (dto.getAge() != null && !dto.getAge().isEmpty())
            return dto.getAge();
        if (dto.getFirstAge() == null)
            return "";
        return switch (dto.getAgeType() == null ? "1" : dto.getAgeType()) {
            case "2" -> dto.getFirstAge() + "月";
            case "3" -> dto.getFirstAge() + "天";
            default -> dto.getFirstAge() + "岁";
        };
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }

    private static void addCellLeftBottom(PdfPTable t, String txt, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(txt, f));
        c.setBorder(Rectangle.BOTTOM);
        c.setBorderWidth(0.5f);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setPadding(2);
        t.addCell(c);
    }

    private static void addCellRightBottom(PdfPTable t, String txt, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(txt, f));
        c.setBorder(Rectangle.BOTTOM);
        c.setBorderWidth(0.5f);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setPadding(2);
        t.addCell(c);
    }

    private static void addCellLeft(PdfPTable t, String txt, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(txt, f));
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setPadding(2);
        t.addCell(c);
    }

    private static void addCellCenter(PdfPTable t, String txt, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(txt, f));
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setPadding(2);
        t.addCell(c);
    }

    private static void addCellRight(PdfPTable t, String txt, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(txt, f));
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setPadding(2);
        t.addCell(c);
    }

    private static void addCellNoBorder(PdfPTable t, String txt, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(txt, f));
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setPadding(0);
        t.addCell(c);
    }

    public static void main(String[] args) throws Exception {
        List<PrescriptionItemDTO> itemList = new ArrayList<>();
        String[] medicineName = {
                "金银花", "连翘", "板蓝根", "蒲公英", "紫花地丁", "黄芩", "黄连", "黄柏",
                "栀子", "石膏", "知母", "麦冬", "天冬", "玄参", "生地", "丹皮", "赤芍", "白芍", "当归", "川芎",
                "黄芪", "党参", "白术", "茯苓", "甘草", "陈皮", "半夏", "厚朴", "枳壳", "桔梗", "杏仁", "薄荷", "防风", "荆芥", "柴胡",
                "香附", "郁金", "延胡索", "红花", "桃仁"
        };
        String[] dosageArr = { "10g", "12g", "15g", "9g", "6g", "20g" };
        String[] decoctArr = { "先煎", "后下", "包煎", "烊化", "冲服", "" };

        for (int i = 0; i < medicineName.length; i++) {
            PrescriptionItemDTO item = new PrescriptionItemDTO();
            item.setItemName(medicineName[i]);
            item.setDosage(dosageArr[i % dosageArr.length]);
            item.setDecoctWay(decoctArr[i % decoctArr.length]);
            itemList.add(item);
        }

        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setPrescNo("TCM-20260425-003");
        dto.setPatientName("李某某");
        dto.setGender("女");
        dto.setAge("36岁");
        dto.setOrderTime(LocalDateTime.now());
        dto.setDoctor("曾医生");
        dto.setAllergicHistory("无");
        dto.setDiagnosis("风热感冒、痰湿内阻");
        dto.setTotalPrice(new java.math.BigDecimal("268.50"));

        dto.setTotalDosage("5剂");
        dto.setDailyDosage("每日1剂，水煎内服");
        dto.setMedicalAdvice("忌烟酒、辛辣、油腻食物，早晚温服");
        dto.setItems(itemList);

        byte[] pdfBytes = generatePrescriptionPdf(List.of(dto), true);
        Path path = Paths.get("tcm_prescription_final.pdf");
        System.out.println("保存PDF文件到：" + path);
        Files.write(path, pdfBytes);
        System.out.println("✅ 生成成功！每页都正常显示（以下空白）！");
    }
}