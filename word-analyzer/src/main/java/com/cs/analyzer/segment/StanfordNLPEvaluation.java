package com.cs.analyzer.segment;

import com.google.common.collect.Lists;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.apache.commons.io.output.NullOutputStream;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: CS
 * @date: 2021/7/21 下午3:06
 * @description: StanfordNLP
 */
@Component
public class StanfordNLPEvaluation implements WordSegment {

    private static final StanfordCoreNLP CTB = new StanfordCoreNLP("StanfordCoreNLP-chinese.properties");
    //    private static final StanfordCoreNLP PKU = new StanfordCoreNLP("StanfordCoreNLP-chinese-pku");
    private static final PrintStream NULL_PRINT_STREAM = new PrintStream(new NullOutputStream(), false);

    private static String seg(StanfordCoreNLP stanfordCoreNLP, String text) {
        PrintStream err = System.err;
        System.setErr(NULL_PRINT_STREAM);
        Annotation document = new Annotation(text);
        stanfordCoreNLP.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        StringBuilder result = new StringBuilder();
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                ;
                result.append(word).append(" ");
            }
        }
        System.setErr(err);
        return result.toString();
    }

    @Override
    public List<String> segList(String text) {
        List<String> result = Lists.newArrayList();
        Annotation document = new Annotation(text);
        CTB.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                result.add(word);
            }
        }
        return result;
    }

    @Override
    public Map<String, String> segMore(String text) {
        Map<String, String> map = new HashMap<>();
//        map.put("Stanford Beijing University segmentation", seg(PKU, text));
        map.put("Stanford Chinese Treebank segmentation", seg(CTB, text));
        return map;
    }
}
