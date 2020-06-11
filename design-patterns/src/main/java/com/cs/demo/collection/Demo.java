package com.cs.demo.collection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: CS
 * @Date: 2020/6/6 11:38 上午
 * @Description:
 */
public class Demo {


    public static void main(String[] args) {
        LinkedList linkedList = new LinkedList();
        List list = new ArrayList(10);
        Vector vector = new Vector(2, 10);
        Set set = new HashSet();
        HashMap hashMap = new HashMap(8);
        Hashtable hashtable = new Hashtable();
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(8);
    }

}
