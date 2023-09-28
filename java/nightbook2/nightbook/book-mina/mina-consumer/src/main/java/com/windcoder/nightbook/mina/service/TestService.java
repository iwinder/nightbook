package com.windcoder.nightbook.mina.service;

import com.windcoder.nightbook.mina.utils.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TestService {
    public  Integer count = 0;
    public  AtomicInteger countn = new AtomicInteger();
    public static Integer count2 = 0;
    public static Integer count3 = 0;
    public static Integer count4 = 0;
    private Set<Integer> numSet = new HashSet<Integer>();

    private static  Set<Integer> numSet2 = new HashSet<Integer>();

    public Set<Integer> add(){
        count++;
        int num = numSet.size();

        countn.incrementAndGet();
        System.out.println(countn+" num "+num);
        for (int i=num;i<num+10;i++){
            numSet.add(i);
            System.out.println("Atomic:"+countn+"count:"+count+" numSet:"+i);
        }

        System.out.println("add count: "+count);
        return numSet;
    }

    public  Set<Integer> add2(){
        count2++;
        int num = numSet2.size();
        for (int i=num;i<num+10;i++){
            numSet2.add(i);
            System.out.println(count2+"numSet2:"+i);
        }

        System.out.println("add2 count2: "+count2);
        return numSet2;
    }

    public void add3(){
        count3++;
        for (int i=0;i<10;i++){
            TestUtil.add(count3);
        }
        System.out.println("add2 count2: "+count3);
    }

    public void add4(){
        count4++;
        for (int i=0;i<10;i++){
            TestUtil.add2(i,count4);
        }

        System.out.println("add2 count2: "+count4);
    }

}
