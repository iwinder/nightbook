package com.windcoder.common.utills;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-09-29
 * Time: 19:45 下午
 */
public class StringUtillZ {


    public static boolean allIsNotNull(String...str) {

        if (str == null || str.length == 0) {
            return false;
        }
        int tmpLen = str.length;
        for (int i=0;i<tmpLen;i++){
            if (str[i] == null){
                return false;
            }
        }
        return true;
    }

    public static boolean allIsNotEmpyty(String...str) {

        if (str == null || str.length == 0) {
            return false;
        }
        int tmpLen = str.length;
        for (int i = 0; i < tmpLen; i++) {
            if (str[i] == null || str[i].length()==0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(allIsNotEmpyty("ss",""));
    }

}
