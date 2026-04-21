package com.qkplm.clinic.libcommon.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class BqPinyinUtils {
    /**
     * 格式化配置：小写、无声调
     */
    private static final HanyuPinyinOutputFormat DEFAULT_FORMAT;

    static {
        DEFAULT_FORMAT = new HanyuPinyinOutputFormat();
        // 小写输出（UPPERCASE 为大写）
        DEFAULT_FORMAT.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        // 不显示拼音声调
        DEFAULT_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    /**
     * 获取单个汉字的拼音首字母
     * 
     * @param word 汉字字符
     * @return 首字母，非汉字返回原字符
     */
    public static String getFirstLetter(char word) {
        try {
            // 判断是否为汉字
            if (Character.toString(word).matches("[\\u4E00-\\u9FA5]")) {
                // 获取汉字拼音（多音字返回数组）
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word, DEFAULT_FORMAT);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    // 取第一个拼音的首字母
                    return String.valueOf(pinyinArray[0].charAt(0));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        // 非汉字直接返回
        return String.valueOf(word);
    }

    /**
     * 获取字符串所有汉字的拼音首字母
     * 示例：中国 -> zg
     * 
     * @param str 输入字符串
     * @return 首字母字符串
     */
    public static String getAllFirstLetters(String str) {
        if (str == null || str.trim().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            sb.append(getFirstLetter(c));
        }
        return sb.toString();
    }

    /**
     * 获取字符串第一个汉字的首字母
     * 示例：中国 -> z
     * 
     * @param str 输入字符串
     * @return 首字母
     */
    public static String getFirstCharLetter(String str) {
        if (str == null || str.trim().isEmpty()) {
            return "";
        }
        return getFirstLetter(str.charAt(0));
    }

    // 测试
    public static void main(String[] args) {
        System.out.println(getAllFirstLetters("中国")); // zg
        System.out.println(getFirstCharLetter("中华人民共和国")); // z
        System.out.println(getAllFirstLetters("SpringBoot 中文测试")); // springboot zwcs
    }
}
