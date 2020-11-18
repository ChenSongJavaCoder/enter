package com.cs.common.test;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: CS
 * @Date: 2020/8/18 3:20 下午
 * @Description:
 */
public class JavaBasicTest {

    public static void main(String[] args) {

        Integer a = new Integer(1);
        Integer b = new Integer(1);
        Integer c = 1;
        Integer d = 1;
        Integer e = Integer.valueOf(1);
        Integer f = Integer.valueOf(1);

//        Assert.isTrue(a == b,"不相等");
        Assert.isTrue(a.equals(b),"不相等");
        Assert.isTrue(c == d,"不相等");
        Assert.isTrue(e == f,"不相等");

        String s    = new String();
//        s.wait();
//        Thread.sleep(100);
        Collections.synchronizedList(new ArrayList<>());
        ArrayList arrayList = new ArrayList();
        new CopyOnWriteArrayList();

        HashMap hashMap = new HashMap(8);
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(8);
        ReentrantLock reentrantLock = new ReentrantLock();

    }
}
