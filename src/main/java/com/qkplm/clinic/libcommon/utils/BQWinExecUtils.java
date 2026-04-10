/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author: Wcke
 * @description: windows exec utils
 * @datetime: 2024-07-05 08:31
 */
public class BQWinExecUtils {
    public static Process exec(String[] args) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            processBuilder.redirectErrorStream(true);
            return processBuilder.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Process exec(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            return processBuilder.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String execWithResult(String command) {
        try {
            Process process = exec(command);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
                return output.toString();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
