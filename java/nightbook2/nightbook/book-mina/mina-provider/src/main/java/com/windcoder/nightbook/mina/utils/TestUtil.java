package com.windcoder.nightbook.mina.utils;

public class TestUtil {
    public static Long count= Long.valueOf(0);

    public  Long count2= Long.valueOf(0);



    public static void add(Integer i){
        count++;
        System.out.println(i+"count:"+count);
    }

    public static void add2(Integer id,Integer i){
        Integer ids = id;
       System.out.println(i+"ids"+ids);

    }
}
