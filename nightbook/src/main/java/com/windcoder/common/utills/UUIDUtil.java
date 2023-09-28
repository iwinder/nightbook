package com.windcoder.common.utills;

import java.util.UUID;

/**
 * Created by wind on 2016/12/26.
 */
public class UUIDUtil {
    public static String newUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


}
