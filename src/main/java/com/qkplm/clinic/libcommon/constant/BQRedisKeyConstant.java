/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.constant;

import com.qkplm.clinic.libcommon.config.BQApplicationContext;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-15 16:32
 */
public class BQRedisKeyConstant {
    static private final String ONLINE_USER_GROUP = "%s:online:users";
    static private final String ONLINE_USER_PREFIX = "%s:%s:%s";

    static private String onlineUserGroupKey() {
        String prefix = BQApplicationContext.getRedisUsersPrefix();
        return String.format(ONLINE_USER_GROUP, prefix);
    }

    static public String onlineUserKey(String account, String md5Value) {
        return String.format(ONLINE_USER_PREFIX, onlineUserGroupKey(), account, md5Value);
    }

    static public String onlineUsersAllKey() {
        return String.format("%s:*", onlineUserGroupKey());
    }
}
