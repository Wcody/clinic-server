package com.qkplm.clinic.libcommon.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 获取单个汉字所有可能的拼音首字母（处理多音字）
     */
    private static List<String> getFirstLetterOptions(char word) {
        try {
            if (Character.toString(word).matches("[\\u4E00-\\u9FA5]")) {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word, DEFAULT_FORMAT);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    return Arrays.stream(pinyinArray)
                            .map(p -> String.valueOf(p.charAt(0)))
                            .distinct()
                            .collect(Collectors.toList());
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return Collections.singletonList(String.valueOf(word));
    }

    /**
     * 获取字符串所有多音字组合的首字母，多个组合用逗号分隔
     * 示例："重庆" -> "ZQ,CQ"（重：zhong/chong，庆：qing）
     */
    public static String getAllFirstLettersPolyphonic(String str) {
        if (str == null || str.trim().isEmpty()) {
            return "";
        }
        List<List<String>> letterOptions = new ArrayList<>();
        for (char c : str.toCharArray()) {
            letterOptions.add(getFirstLetterOptions(c));
        }
        // 笛卡尔积
        List<String> combinations = new ArrayList<>();
        combinations.add("");
        for (List<String> options : letterOptions) {
            List<String> next = new ArrayList<>();
            for (String existing : combinations) {
                for (String opt : options) {
                    next.add(existing + opt);
                }
            }
            combinations = next;
        }
        return combinations.stream().distinct().collect(Collectors.joining(","));
    }

    // 测试
    public static void main(String[] args) {
        System.out.println(getAllFirstLetters("中国")); // ZG
        System.out.println(getFirstCharLetter("中华人民共和国")); // Z
        System.out.println(getAllFirstLettersPolyphonic("重庆")); // ZQ,CQ
    }
}
