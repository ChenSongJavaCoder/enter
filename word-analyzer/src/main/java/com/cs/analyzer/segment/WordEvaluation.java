package com.cs.analyzer.segment;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: CS
 * @date: 2021/7/21 下午3:34
 * @description:
 */
@Component
public class WordEvaluation implements WordSegment {

    private static String seg(String text, SegmentationAlgorithm segmentationAlgorithm) {
        StringBuilder result = new StringBuilder();
        for (Word word : WordSegmenter.segWithStopWords(text, segmentationAlgorithm)) {
            result.append(word.getText()).append(" ");
        }
        return result.toString();
    }

    @Override
    public List<String> segList(String text) {
        return WordSegmenter.seg(text).stream().map(Word::getText).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> segMore(String text) {
        Map<String, String> map = new HashMap<>();
        for (SegmentationAlgorithm segmentationAlgorithm : SegmentationAlgorithm.values()) {
            map.put(segmentationAlgorithm.getDes(), seg(text, segmentationAlgorithm));
        }
        return map;
    }
}
