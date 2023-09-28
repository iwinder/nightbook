package com.windcoder.nightbook.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Created by wind on 2016/12/24.
 */
public class Base64Util {

    /**
     *对String类型的数据编码
     * @param str
     *         要编码的String类型的数据
     * @return
     */
    public static String encode(String str){
        try {
            return Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  对byte[]类型的数据编码
     * @param asBytes
     *          要编码的byte[]类型的数据
     * @return
     */
    public static String encodeOfByte(byte[] asBytes){
        return Base64.getEncoder().encodeToString(asBytes);
    }
    /**
     *  解码
     *      使用java8
     * @param str
     *         要解码的字符串
     * @return
     */
    public static byte[] decode(String str){
        byte[] asBytes = Base64.getDecoder().decode(str);
        return asBytes;
    }
}
