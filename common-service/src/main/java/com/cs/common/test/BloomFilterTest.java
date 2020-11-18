package com.cs.common.test;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.bloomfilter.BloomFilterUtil;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * @Author: CS
 * @Date: 2020/9/7 4:12 下午
 * @Description: 布隆过滤器
 */
public class BloomFilterTest {


    public static void main(String[] args) {
        BitMapBloomFilter filter = BloomFilterUtil.createBitMap(100000);

        BloomFilter.create(Funnels.integerFunnel(),100000);
    }
}
