/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Formatter;

/**
 * @author: Wcke
 * @description: 设备工具类
 * @datetime: 2024-07-05 07:18
 */
@Slf4j
public class BQDeviceUtils {
    private static final SystemInfo SYSTEMINFO = new SystemInfo();
    private static final HardwareAbstractionLayer HAL = SYSTEMINFO.getHardware();

    public static final String SALT = "www.baqi.tech";
    private static String getCPUSerial() {
        return HAL.getProcessor().getProcessorIdentifier().getProcessorID();
    }

    private static String getMotherboardSerial() {
        return HAL.getComputerSystem().getSerialNumber();
    }

    private static String getMacAddress() {
        ArrayList<String> macs = new ArrayList<>();
        for (NetworkIF net : HAL.getNetworkIFs()) {
            if (!macs.contains(net.getMacaddr())) {
                macs.add(net.getMacaddr());
            }
        }
        //数组排序，防止前后生成标记不一致
        macs.sort(String::compareTo);
        return macs.toString();
    }

    private static String getHardDriveSerial() {
        ArrayList<String> disks = new ArrayList<>();
        for (HWDiskStore disk : HAL.getDiskStores()) {
            disks.add(disk.getSerial());
        }
        //数组排序，防止前后生成标记不一致
        disks.sort(String::compareTo);
        return disks.toString();
    }

    private static String byteArrayToHex(byte[] bytes) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }

    private static String generateDeviceCode(String... components) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        for (String component : components) {
            if (component != null) {
                md.update(component.getBytes());
            }
        }
        return byteArrayToHex(md.digest());
    }

    /*
     * 当前设备的设备码
     */
    public static String getDeviceCode() {
        try {
            String cpuSerial = getCPUSerial();
            //log.info("CPU序列号:{}", cpuSerial);
            String motherboardSerial = getMotherboardSerial();
            //log.info("主板序列号:{}", motherboardSerial);
            String macAddress = getMacAddress();
            //log.info("MAC地址:{}", macAddress);
            String hardDriveSerial = getHardDriveSerial();
            //log.info("硬盘序列号:{}", hardDriveSerial);

            // 返回64位长度的设备码
            return generateDeviceCode(SALT, cpuSerial, motherboardSerial, macAddress, hardDriveSerial, SALT).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
