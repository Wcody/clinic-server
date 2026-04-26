/*
 * 版权声明 Copyright (c) 2026。
 */
package com.qkplm.clinic.libcommon.pdf.openpdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;

/**
 * 最终版：使用你指定的阿里巴巴健康字体 + 粗体字体文件
 */
public class PrescriptionPdfUtil {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    private static final BaseFont BASE_FONT;
    private static final BaseFont BASE_FONT_BOLD;
    private static final float ROW_HEIGHT = 18f;
    private static final int LINES_PER_PAGE = 6;

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
            throw new RuntimeException("字体加载失败，请检查路径：/src/main/resources/fonts/", e);
        }
    }

    // ==================== 字体工具：粗体使用 BOLD 字体文件 ====================
    private static Font titleFont(int size) {
        return new Font(BASE_FONT_BOLD, size + 2);
    }

    private static Font normalFont(int size) {
        return new Font(BASE_FONT_BOLD, size + 2);
    }

    private static Font boldFont(int size) {
        return new Font(BASE_FONT_BOLD, size + 2);
    }

    private static Font regularFont(int size) {
        return new Font(BASE_FONT, size + 2);
    }

    // ==================== @Data 实体类 ====================
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
        private List<PrescriptionItemDTO> items;
    }

    @Data
    public static class PrescriptionItemDTO {
        private Byte itemType;
        private Integer groupNo;
        private String itemName;
        private String spec;
        private java.math.BigDecimal totalNum;
        private String useWay;
        private String singleDosage;
        private String unit;
        private String frequency;
        private Integer days;
        private String entrust;
    }

    // ==================== 公开方法 ====================
    public static byte[] generatePrescriptionPdf(List<PrescriptionDTO> prescriptionList, boolean showPrice) {
        if (prescriptionList == null || prescriptionList.isEmpty()) {
            throw new RuntimeException("处方数据不能为空");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A5, 25, 25, 15, 15);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            PrescriptionPageEvent pageEvent = new PrescriptionPageEvent();
            writer.setPageEvent(pageEvent);
            document.open();
            boolean first = true;
            for (PrescriptionDTO dto : prescriptionList) {
                List<PrescriptionItemDTO> items = Optional.ofNullable(dto.getItems()).orElse(new ArrayList<>());
                int total = items.size();
                int totalPages = Math.max(1, (total + LINES_PER_PAGE - 1) / LINES_PER_PAGE);
                if (!first) document.newPage();
                first = false;
                pageEvent.reset(dto, showPrice, totalPages);
                renderOnePrescription(document, dto);
            }
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF生成失败", e);
        }
    }

    private static void renderOnePrescription(Document document, PrescriptionDTO dto) throws Exception {
        List<PrescriptionItemDTO> items = Optional.ofNullable(dto.getItems()).orElse(new ArrayList<>());
        int total = items.size();
        int current = 0;
        boolean firstPage = true;

        while (current < total) {
            if (!firstPage) document.newPage();
            firstPage = false;
            addHeader(document, dto);
            int end = Math.min(current + LINES_PER_PAGE, total);
            addDrugTable(document, items.subList(current, end));
            current = end;
        }
    }

    // ==================== 头部 ====================
    private static void addHeader(Document document, PrescriptionDTO dto) throws Exception {
        PdfPTable tTitle = new PdfPTable(1);
        tTitle.setWidthPercentage(100);
        addCellCenter(tTitle, "茂名市高州市石鼓镇九罡村曾俊华卫生室", titleFont(13));
        document.add(tTitle);

        PdfPTable tSub = new PdfPTable(1);
        tSub.setWidthPercentage(100);
        addCellCenter(tSub, "处方笺", titleFont(12));
        document.add(tSub);

        PdfPTable t3 = new PdfPTable(2);
        t3.setWidthPercentage(100);
        t3.setWidths(new float[] { 1, 1 });
        addCellLeftBottom(t3, "费别：自费", boldFont(8));
        addCellRightBottom(t3, "处方编号：" + nvl(dto.getPrescNo()), boldFont(8));
        document.add(t3);

        PdfPTable t4 = new PdfPTable(3);
        t4.setWidthPercentage(100);
        t4.setWidths(new float[] { 3, 1, 2 });
        addCellLeft(t4, "姓名：" + nvl(dto.getPatientName()), normalFont(8));
        addCellCenter(t4, "性别：" + nvl(dto.getGender()), normalFont(8));
        addCellRight(t4, "年龄：" + getAgeText(dto), normalFont(8));
        document.add(t4);

        PdfPTable t5 = new PdfPTable(2);
        t5.setWidthPercentage(100);
        addCellLeft(t5, "身份证号：" + nvl(dto.getIdCard()), normalFont(8));
        addCellRight(t5, "门诊编号：" + nvl(dto.getClinicNo()), normalFont(8));
        document.add(t5);

        PdfPTable t6 = new PdfPTable(2);
        t6.setWidthPercentage(100);
        String date = dto.getOrderTime() != null ? dto.getOrderTime().format(DATE_FMT) : "";
        addCellLeft(t6, "联系电话：" + nvl(dto.getPhone()), normalFont(8));
        addCellRight(t6, "开具日期：" + date, normalFont(8));
        document.add(t6);

        PdfPTable tAddr = new PdfPTable(1);
        tAddr.setWidthPercentage(100);
        addCellLeft(tAddr, "地址：" + nvl(dto.getAddress()), normalFont(8));
        document.add(tAddr);

        PdfPTable tAllergy = new PdfPTable(1);
        tAllergy.setWidthPercentage(100);
        String allergy = (dto.getAllergicHistory() == null || dto.getAllergicHistory().isBlank()) ? "无"
                : dto.getAllergicHistory();
        addCellLeft(tAllergy, "过敏史：" + allergy, normalFont(8));
        document.add(tAllergy);

        PdfPTable tDiag = new PdfPTable(1);
        tDiag.setWidthPercentage(100);
        addCellLeft(tDiag, "临床诊断：" + nvl(dto.getDiagnosis()), normalFont(8));
        document.add(tDiag);

        PdfPTable tSuggest = new PdfPTable(1);
        tSuggest.setWidthPercentage(100);
        addCellLeft(tSuggest, "治疗建议：" + nvl(dto.getTreatmentSuggest()), normalFont(8));
        document.add(tSuggest);

        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100);
        PdfPCell c = new PdfPCell(new Phrase(""));
        c.setBorder(Rectangle.BOTTOM);
        c.setBorderWidth(0.5f);
        c.setPadding(1);
        line.addCell(c);
        document.add(line);

        PdfPTable rp = new PdfPTable(1);
        rp.setWidthPercentage(100);
        addCellLeft(rp, "RP", boldFont(12));
        document.add(rp);
    }

    // ==================== 药品表格 ====================
    private static void addDrugTable(Document document, List<PrescriptionItemDTO> pageItems) throws Exception {
        // 4列：组号 | 名称 | 规格 | 用量
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(90);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new float[] { 0.6f, 3.5f, 2f, 1.5f });

        Integer prevGroupNo = null;
        for (PrescriptionItemDTO item : pageItems) {
            boolean showGroup = item.getGroupNo() != null && !item.getGroupNo().equals(prevGroupNo);
            addDrugRow(table, item, showGroup);
            addUsageRow(table, item);
            prevGroupNo = item.getGroupNo();
        }
        // 以下空白：占一个药品行 + 一个用法行
        addBlankMarkRow(table);
        addEmptySpanRow(table);
        int fill = LINES_PER_PAGE - pageItems.size() - 1;
        for (int i = 0; i < fill; i++) {
            addEmptyRow(table);
            addEmptySpanRow(table);
        }
        document.add(table);
    }

    private static void addDrugRow(PdfPTable table, PrescriptionItemDTO item, boolean showGroup) {
        String groupLabel = showGroup && item.getGroupNo() != null ? item.getGroupNo() + "：" : "";
        PdfPCell groupCell = cell(groupLabel);
        groupCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(groupCell);
        table.addCell(cell(nvl(item.getItemName())));
        table.addCell(cell(nvl(item.getSpec())));
        String qty = trimNumber(item.getSingleDosage());
        if (item.getUnit() != null && !item.getUnit().isEmpty()) {
            qty += item.getUnit();
        }
        PdfPCell numCell = cell(qty);
        numCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(numCell);
    }

    /** 用法行：跨 4 列，右对齐，非粗体 */
    private static void addUsageRow(PdfPTable table, PrescriptionItemDTO item) {
        PdfPCell c = new PdfPCell(new Phrase(getUsageText(item), regularFont(10)));
        c.setColspan(4);
        c.setBorder(Rectangle.NO_BORDER);
        c.setFixedHeight(ROW_HEIGHT);
        c.setPaddingRight(6);
        c.setPaddingTop(2);
        c.setPaddingBottom(2);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c);
    }

    private static void addBlankMarkRow(PdfPTable table) {
        PdfPCell c = new PdfPCell(new Phrase("（以下空白）", normalFont(10)));
        c.setColspan(4);
        c.setBorder(Rectangle.NO_BORDER);
        c.setFixedHeight(ROW_HEIGHT);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setPaddingTop(2);
        c.setPaddingBottom(2);
        table.addCell(c);
    }

    private static void addEmptyRow(PdfPTable table) {
        PdfPCell c = new PdfPCell(new Phrase(" ", normalFont(10)));
        c.setColspan(4);
        c.setBorder(Rectangle.NO_BORDER);
        c.setFixedHeight(ROW_HEIGHT);
        table.addCell(c);
    }

    /** 空的用法占位行（配合 addEmptyRow / addBlankMarkRow 保持行高一致） */
    private static void addEmptySpanRow(PdfPTable table) {
        PdfPCell c = new PdfPCell(new Phrase(" ", normalFont(10)));
        c.setColspan(4);
        c.setBorder(Rectangle.NO_BORDER);
        c.setFixedHeight(ROW_HEIGHT);
        table.addCell(c);
    }

    // ==================== 底部 ====================
    private static void addFooter(PdfContentByte cb, Rectangle pageSize, PrescriptionDTO dto,
            boolean showPrice, int currentPage, int totalPage) {
        float left = 25f;
        float width = pageSize.getWidth() - 50;
        float y = pageSize.getBottom() + 40f;

        PdfPTable t1 = new PdfPTable(3);
        t1.setTotalWidth(width);
        t1.setLockedWidth(true);
        t1.setWidths(new float[] { 1, 1, 1 });
        addCellNoBorder(t1, "审核：", normalFont(7));
        addCellNoBorder(t1, "调配：", normalFont(7));
        addCellNoBorder(t1, "核对发药：", normalFont(7));
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
        addCellNoBorder(t2, "医生：" + nvl(dto.getDoctor()), normalFont(7));
        addCellNoBorder(t2, "总金额：" + priceText, boldFont(7));
        addCellNoBorder(t2, "医师签名：", normalFont(7));
        t2.writeSelectedRows(0, -1, left, y, cb);
        y += t2.getTotalHeight() + 11;

        PdfPTable t3 = new PdfPTable(1);
        t3.setTotalWidth(width);
        t3.setLockedWidth(true);
        PdfPCell pageCell = new PdfPCell(new Phrase("第" + currentPage + "页，共" + totalPage + "页", normalFont(7)));
        pageCell.setBorder(Rectangle.BOTTOM);
        pageCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pageCell.setPadding(4);
        t3.addCell(pageCell);
        t3.writeSelectedRows(0, -1, left, y, cb);
    }

    // ==================== 工具方法 ====================
    private static PdfPCell cell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, normalFont(10)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingTop(2);
        cell.setPaddingBottom(2);
        return cell;
    }

    private static String trimNumber(String s) {
        if (s == null || s.isEmpty()) return s == null ? "" : s;
        try {
            return new java.math.BigDecimal(s).stripTrailingZeros().toPlainString();
        } catch (NumberFormatException e) {
            return s;
        }
    }

    private static String getUsageText(PrescriptionItemDTO item) {
        if (item == null) return "";
        StringBuilder sb = new StringBuilder();
        if (item.getUseWay() != null && !item.getUseWay().isEmpty())
            sb.append(item.getUseWay());
        if (item.getSingleDosage() != null && !item.getSingleDosage().isEmpty()) {
            sb.append(" 每次").append(trimNumber(item.getSingleDosage()));
            if (item.getUnit() != null && !item.getUnit().isEmpty())
                sb.append(item.getUnit());
        }
        if (item.getFrequency() != null && !item.getFrequency().isEmpty())
            sb.append(" ").append(item.getFrequency());
        if (item.getDays() != null)
            sb.append(" 共").append(item.getDays()).append("天");
        if (item.getEntrust() != null && !item.getEntrust().isEmpty()) {
            if (sb.length() > 0)
                sb.append("（").append(item.getEntrust()).append("）");
            else
                sb.append(item.getEntrust());
        }
        return sb.toString();
    }

    private static String getAgeText(PrescriptionDTO dto) {
        if (dto.getAge() != null && !dto.getAge().isEmpty())
            return dto.getAge();
        if (dto.getFirstAge() == null)
            return "";
        String unit = switch (dto.getAgeType() == null ? "1" : dto.getAgeType()) {
            case "2" -> "月";
            case "3" -> "天";
            default -> "岁";
        };
        return dto.getFirstAge() + unit;
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

    // ==================== 页脚事件 ====================
    static class PrescriptionPageEvent extends com.lowagie.text.pdf.PdfPageEventHelper {
        private PrescriptionDTO dto;
        private boolean showPrice;
        private int totalPages;
        private int pageIndex;

        void reset(PrescriptionDTO dto, boolean showPrice, int totalPages) {
            this.dto = dto;
            this.showPrice = showPrice;
            this.totalPages = totalPages;
            this.pageIndex = 0;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            if (dto == null) return;
            pageIndex++;
            addFooter(writer.getDirectContent(), writer.getPageSize(), dto, showPrice, pageIndex, totalPages);
        }
    }

    // ==================== 测试 ====================
    public static void main(String[] args) throws Exception {
        List<PrescriptionItemDTO> items = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            PrescriptionItemDTO item = new PrescriptionItemDTO();
            item.setItemType((byte) 1);
            item.setItemName("药品" + i);
            item.setSpec("0.5g×12粒");
            item.setTotalNum(new java.math.BigDecimal("1"));
            item.setUseWay("口服");
            item.setSingleDosage("1");
            item.setUnit("粒");
            item.setFrequency("tid");
            item.setDays(3);
            items.add(item);
        }

        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setPrescNo("R202604250001");
        dto.setPatientName("测试患者");
        dto.setGender("男");
        dto.setAge("45岁");
        dto.setOrderTime(LocalDateTime.now());
        dto.setDoctor("张医生");
        dto.setAllergicHistory("无");
        dto.setDiagnosis("上呼吸道感染");
        dto.setTotalPrice(new java.math.BigDecimal("225.00"));
        dto.setItems(items);

        byte[] pdf = generatePrescriptionPdf(List.of(dto), true);
        Files.write(Paths.get("prescription_final.pdf"), pdf);
        System.out.println("PDF 生成完成 ✅");
    }
}