/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author: Wcke
 * @description: 图片常用工具类
 * @datetime: 2025-01-01 13:57
 */
@Slf4j
public class BQImageUtils {
    public static byte[] cropImage(String imagePath, int left, int top, int width, int height) {
        return cropImage(new File(imagePath), left, top, width, height);
    }

    public static byte[] cropImage(File imageFile, int left, int top, int width, int height) {
        // 读取原图片
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            log.error("裁剪图片时发生异常", e);
            throw new RuntimeException(e);
        }

        // 检查裁剪区域是否超出图片边界
        if (left < 0 || top < 0 || left + width > originalImage.getWidth() || top + height > originalImage.getHeight()) {
            throw new IllegalArgumentException("裁剪区域超出图片范围");
        }

        // 裁剪图片
        BufferedImage croppedImage = originalImage.getSubimage(left, top, width, height);

        // 将裁剪后的图片写入字节数组输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String imageName = imageFile.getName();
        try {
            // 根据名称获取后缀
            String formatName = imageName.substring(imageName.lastIndexOf(".") + 1);
            ImageIO.write(croppedImage, formatName, outputStream); // 可根据需求改为 jpg 或其他格式
        } catch (IOException e) {
            log.error("裁剪图片时发生异常", e);
            throw new RuntimeException(e);
        }

        // 返回字节数组
        return outputStream.toByteArray();
    }
}
