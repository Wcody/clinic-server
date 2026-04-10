/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qkplm.clinic.libcommon.base.BQButton;
import lombok.val;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: Wcke
 * @description: 反射工具类
 * @datetime: 2024-06-27 20:42
 */
public class BQReflectionUtils {
    public static ObjectNode getEnumButtons(String packagePath) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ObjectNode ret = BQJacksonUtils.newObjectNode();
        // 创建Reflections对象
        Reflections reflections = new Reflections(packagePath, Scanners.SubTypes.filterResultsBy(s -> true));
        // 获取指定包路径下的所有枚举类
        var enumClasses = reflections.getSubTypesOf(Enum.class);
        // 遍历每个枚举类
        for (val enumClass : enumClasses) {
            //提取分组名称
            String className = enumClass.getSimpleName();
            className = className.substring(2, className.length() - 7);
            className = className.substring(0, 1).toLowerCase() + className.substring(1);
            ArrayNode arrayNode = BQJacksonUtils.newArrayNode();
            ret.set(className, arrayNode);
            //方法名称
            Method getValueMethod = enumClass.getMethod("getValue");
            Method getLabelMethod = enumClass.getMethod("getLabel");
            // 获取并遍历枚举类的所有枚举值
            Enum<?>[] enumConstants = enumClass.getEnumConstants();
            if (enumConstants != null) {
                for (Enum<?> enumConstant : enumConstants) {
                    String value = (String) getValueMethod.invoke(enumConstant);
                    String label = (String) getLabelMethod.invoke(enumConstant);
                    arrayNode.add(BQJacksonUtils.newObjectNode().put(value, label));
                }
            }
        }
        return ret;
    }

    public static ObjectNode getConstantButtons(String packagePath) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        ObjectNode ret = BQJacksonUtils.newObjectNode();
        // 创建Reflections对象
        Reflections reflections = new Reflections(packagePath, Scanners.SubTypes.filterResultsBy(s -> true));
        // 获取指定包路径下的所有枚举类
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        // 遍历每个枚举类
        for (Class<?> clazz : classes) {
            //提取分组名称
            String className = "";
            // 按照顺序获取所有静态字段
            List<String> keys = getKeys(clazz);

            ArrayNode arrayNode = BQJacksonUtils.newArrayNode();
            // 前面是为了获取按钮的定义顺序
            // 获取map
            Field mapField = clazz.getDeclaredField("map");
            // 允许访问私有字段
            mapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, BQButton> map = (Map<String, BQButton>) mapField.get(null);
            // 按顺序遍历
            for (String key : keys) {
                BQButton button = map.get(key);
                if (button != null) {
                    if (className.isEmpty()) {
                        // 只取第一个
                        className = button.getName().split(":")[0];
                    }
                    arrayNode.addPOJO(button);
                }
            }

            ret.set(className, arrayNode);
        }
        return ret;
    }

    private static List<String> getKeys(Class<?> clazz) throws IllegalAccessException {
        List<String> keys = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            // 检查是否是 static 和 final
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                //System.out.println(field.getType().getName());
                // 检查是否是 String 类型
                if (field.getType().equals(String.class)) {
                    // 获取字段值
                    // static 字段传入 null
                    String value = (String) field.get(null);
                    keys.add(value);
                }
            }
        }
        return keys;
    }
}
