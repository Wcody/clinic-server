/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import com.qkplm.clinic.libcommon.entity.BQPDFImageLabel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: Wcke
 * @description: PDF工具类
 * @datetime: 2025-01-20 15:14
 */
@Slf4j
public class BQPDFUtils {
    /**
     * PDF 转换图片
     *
     * @param pdfFilePath  PDF文件路径
     * @param imageFilePath 图片文件路径
     * @return 图片文件列表
     */
    public static ArrayList<File> extractPDF2Image(String pdfFilePath, String imageFilePath) {
        ArrayList<File> imageFiles = new ArrayList<>();
        try (PDDocument document = Loader.loadPDF(new File(pdfFilePath))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300); // 300 DPI 高质量
                File outputFile = new File(imageFilePath, page + ".png");
                ImageIO.write(image, "PNG", outputFile);
                imageFiles.add(outputFile);
            }
            return imageFiles;
        } catch (IOException e) {
            log.error("PDF 转换图片失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建一个页面并在页面上插入图片和标签
     * @param document PDF 文档
     * @param imagePath 图片文件路径
     * @param label 标签文本
     * @return 创建的页面
     */
    private static PDPage createImagePage(PDDocument document, String imagePath, String label) throws IOException {
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();

            // 加载图片
            BufferedImage img = ImageIO.read(new File(imagePath));
            PDImageXObject pdImage = PDImageXObject.createFromFileByContent(new File(imagePath), document);

            // 根据页面宽度调整图片的宽度，保持宽高比
            float imageWidth = pageWidth;
            float imageHeight = (img.getHeight() / (float) img.getWidth()) * imageWidth;

            // 如果图片高度大于页面高度，则调整图片大小
            if (imageHeight > pageHeight - 100) {
                imageHeight = pageHeight - 100;
                imageWidth = (img.getWidth() / (float) img.getHeight()) * imageHeight;
            }

            // 将图片插入页面
            contentStream.drawImage(pdImage, 0, pageHeight - imageHeight, imageWidth, imageHeight);
        }

        return page;
    }

    /**
     * 为每个页面创建书签
     * @param outline 书签容器
     * @param page 页面
     * @param label 书签的标题
     */
    private static void createBookmark(PDDocumentOutline outline, PDPage page, String label) {
        PDOutlineItem item = new PDOutlineItem();
        item.setTitle(label);  // 设置书签标题
        item.setDestination(page);  // 设置书签目标页面
        outline.addLast(item);  // 将书签添加到书签容器
    }

    /**
     * 将图片列表转换为 PDF 文件
     * @param images 图片列表
     * @param pdfFilePath PDF 文件路径
     */
    public static void imageList2PDF(List<BQPDFImageLabel> images, String pdfFilePath) {
        // 创建 PDF 文档
        try (PDDocument document = new PDDocument()) {

            // 创建书签容器
            PDDocumentOutline outline = new PDDocumentOutline();
            document.getDocumentCatalog().setDocumentOutline(outline);

            // 为每个图片创建页面并插入内容
            HashMap<String, Integer> pageMap = new HashMap<>();
            for (BQPDFImageLabel imageLabel : images) {
                PDPage page = createImagePage(document, imageLabel.getImagePath(), imageLabel.getLabel());
                String label = imageLabel.getLabel();
                if (StringUtils.isNotBlank(label) && !pageMap.containsKey(label)) {
                    createBookmark(outline, page, label);
                    pageMap.put(label, 1);
                }
            }

            // 保存 PDF 文件
            document.save(pdfFilePath);
        } catch (IOException e) {
            log.error("图片列表转换PDF失败", e);
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) {
////        ArrayList<File> files = extractPDF2Image("D:\\test\\1.pdf", "D:\\test");
////        for (File file : files) {
////            System.out.println(file.getAbsolutePath());
////        }
//
//        List<BQPDFImageLabel> imageLabels = List.of(
//                new BQPDFImageLabel("D:\\test\\0dd5a7d856bc4110a469b3af20ec56c8.png", "病案首页"),
//                new BQPDFImageLabel("D:\\test\\1c7fd6526ebe4ec5971b773d87bd7f30.png", "病案首页"),
//                new BQPDFImageLabel("D:\\test\\02fd13fa512a4fe68477c5df9f4c38ae.png", "病案首页"),
//                new BQPDFImageLabel("D:\\test\\2b6a4cb00cfe47dca4689736800fa1f2.png", "同意书"),
//                new BQPDFImageLabel("D:\\test\\2ca711361e40447eacdac55d85df19ce.png", "意见书"),
//                new BQPDFImageLabel("D:\\test\\2ce67ece77164c23a985cc461476849d.png", "")
//        );
//
//        imageList2PDF(imageLabels, "D:\\test\\output.pdf");
//    }

    public static void main(String[] args) throws IOException {
        String inputPdfPath = "D:\\sign\\input.pdf";  // 输入 PDF 文件路径
        String outputPdfPath = "D:\\sign\\output.pdf"; // 输出 PDF 文件路径
        String targetText = "签名位置"; // 需要替换的文本
        String imagePath = "D:\\sign\\sign.png"; // 需要替换的图片

        // 加载 PDF
        try (PDDocument document = Loader.loadPDF(new File(inputPdfPath))) {
            PDPage page = document.getPage(0); // 处理第一页

            // 创建 PDFTextStripper 提取文本
            PDFTextStripper stripper = new PDFTextStripper() {
                @Override
                protected void processTextPosition(TextPosition text) {
                    super.processTextPosition(text);
                    // 输出所有文本位置数据，方便调试
                    System.out.println("Text: " + text.getUnicode() + " at [" + text.getX() + ", " + text.getY() + "] with width: " + text.getWidth());

                    // 查找目标文本
                    if (text.getUnicode().equals(targetText)) {
                        // 获取目标文本的坐标
                        float x = text.getX();
                        float y = text.getY();
                        float width = text.getWidth();
                        float height = text.getHeight();

                        System.out.println("找到目标文本位置：x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);

                        // 在该位置插入图片
                        try {
                            PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
                            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                                contentStream.drawImage(image, x, y - image.getHeight(), width, height);  // 调整图片位置和大小
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            // 设置文本提取区域
            stripper.setStartPage(1);
            stripper.setEndPage(1);
            stripper.getText(document);

            // 保存修改后的 PDF
            document.save(outputPdfPath);
            System.out.println("PDF 处理完成，已保存到：" + outputPdfPath);
        }
    }
//public static void main(String[] args) throws IOException {
//    ArrayList<File> files = extractPDF2Image("D:\\test\\666\\a.pdf", "D:\\test\\666");
//    for (File file : files) {
//        System.out.println(file.getAbsolutePath());
//    }
//        List<BQPDFImageLabel> imageLabels = List.of(
//                new BQPDFImageLabel("D:\\test\\images\\0.png", "临床病历"),
//                new BQPDFImageLabel("D:\\test\\images\\1.png", "临床病历"),
//                new BQPDFImageLabel("D:\\test\\images\\2.png", "临床病历"),
//                new BQPDFImageLabel("D:\\test\\images\\3.png", "临床病历")
//        );
//    imageList2PDF(imageLabels, "D:\\test\\555.pdf");
//    String inputPdfPath = "D:\\sign\\big.spf";  // 输入 PDF 文件路径
//    String outputPdfPath = "D:\\sign\\output.pdf"; // 输出 PDF 文件路径
//    String targetText = "签名位置"; // 需要替换的文本
//    String imagePath = "D:\\sign\\sign.png"; // 需要替换的图片
//
//    // 加载 PDF
//    try (PDDocument document = Loader.loadPDF(new File(inputPdfPath))) {
//        for (int i = 0; i < document.getNumberOfPages(); i++) {
//            PDPage page = document.getPage(i); // 处理第一页
//
//            // 以追加模式打开内容流
//            PDPageContentStream contentStream = new PDPageContentStream(
//                    document, page, PDPageContentStream.AppendMode.APPEND, true
//            );
//            contentStream.beginText();
//            contentStream.setFont(PDType0Font.load(document, new File("d:\\sign\\simsun.ttf")), 12); // 加载中文字体
//            contentStream.newLineAtOffset(100, 500);
//            contentStream.showText("追加的中文内容admin123");
//            contentStream.endText();
//            contentStream.close();
//        }
//        document.save("d:\\sign\\updated.pdf");
//        System.out.println("PDF 处理完成，已保存到：" + outputPdfPath);
//    }
}
