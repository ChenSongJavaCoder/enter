package com.cs.analyzer.segment;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: CS
 * @date: 2021/7/21 下午3:04
 * @description:
 */
public interface WordSegment {
    /**
     * 获取文本的所有分词结果
     *
     * @param text 文本
     * @return 所有的分词结果，去除重复
     */
    default Set<String> seg(String text) {
        return segMore(text).values().stream().collect(Collectors.toSet());
    }

    /**
     * 分词成数组
     *
     * @param text
     * @return
     */
    List<String> segList(String text);

    /**
     * 获取文本的所有分词结果
     *
     * @param text 文本
     * @return 所有的分词结果，KEY 为分词器模式，VALUE 为分词器结果
     */
    Map<String, String> segMore(String text);
}
