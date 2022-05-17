package com.cs.analyzer.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * @author: CS
 * @date: 2021/4/30 下午3:06
 * @description: 余玄相似度计算
 */
public class CosUtil {

    //阈值
    public static double YUZHI = 0.2;

    public static double getSimilarity(String doc1, String doc2) {
        if (doc1 != null && doc1.trim().length() > 0 && doc2 != null
                && doc2.trim().length() > 0) {

            Map<Integer, int[]> AlgorithmMap = new HashMap<Integer, int[]>();

            //将两个字符串中的中文字符以及出现的总数封装到，AlgorithmMap中
            for (int i = 0; i < doc1.length(); i++) {
                char d1 = doc1.charAt(i);
                if (isHanZi(d1)) {
                    int charIndex = getGB2312Id(d1);
                    if (charIndex != -1) {
                        int[] fq = AlgorithmMap.get(charIndex);
                        if (fq != null && fq.length == 2) {
                            fq[0]++;
                        } else {
                            fq = new int[2];
                            fq[0] = 1;
                            fq[1] = 0;
                            AlgorithmMap.put(charIndex, fq);
                        }
                    }
                }
            }

            for (int i = 0; i < doc2.length(); i++) {
                char d2 = doc2.charAt(i);
                if (isHanZi(d2)) {
                    int charIndex = getGB2312Id(d2);
                    if (charIndex != -1) {
                        int[] fq = AlgorithmMap.get(charIndex);
                        if (fq != null && fq.length == 2) {
                            fq[1]++;
                        } else {
                            fq = new int[2];
                            fq[0] = 0;
                            fq[1] = 1;
                            AlgorithmMap.put(charIndex, fq);
                        }
                    }
                }
            }

            Iterator<Integer> iterator = AlgorithmMap.keySet().iterator();
            double sqdoc1 = 0;
            double sqdoc2 = 0;
            double denominator = 0;
            while (iterator.hasNext()) {
                int[] c = AlgorithmMap.get(iterator.next());
                denominator += c[0] * c[1];
                sqdoc1 += c[0] * c[0];
                sqdoc2 += c[1] * c[1];
            }

            return denominator / Math.sqrt(sqdoc1 * sqdoc2);
        } else {
            throw new NullPointerException(
                    " the Document is null or have not cahrs!!");
        }
    }

    public static boolean isHanZi(char ch) {
        // 判断是否汉字
        return (ch >= 0x4E00 && ch <= 0x9FA5);

    }

    /**
     * 根据输入的Unicode字符，获取它的GB2312编码或者ascii编码，
     *
     * @param ch 输入的GB2312中文字符或者ASCII字符(128个)
     * @return ch在GB2312中的位置，-1表示该字符不认识
     */
    public static short getGB2312Id(char ch) {
        try {
            byte[] buffer = Character.toString(ch).getBytes("GB2312");
            if (buffer.length != 2) {
                // 正常情况下buffer应该是两个字节，否则说明ch不属于GB2312编码，故返回'?'，此时说明不认识该字符
                return -1;
            }
            int b0 = (int) (buffer[0] & 0x0FF) - 161; // 编码从A1开始，因此减去0xA1=161
            int b1 = (int) (buffer[1] & 0x0FF) - 161; // 第一个字符和最后一个字符没有汉字，因此每个区只收16*6-2=94个汉字
            return (short) (b0 * 94 + b1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 返回百分比
     *
     * @param T1
     * @param T2
     * @return
     * @author: Administrator
     * @Date: 2015年1月22日
     */
    public static double getSimilarity(Vector<String> T1, Vector<String> T2) throws Exception {
        int size = 0, size2 = 0;
        if (T1 != null && (size = T1.size()) > 0 && T2 != null && (size2 = T2.size()) > 0) {

            Map<String, double[]> T = new HashMap<String, double[]>();

            //T1和T2的并集T
            String index = null;
            for (int i = 0; i < size; i++) {
                index = T1.get(i);
                if (index != null) {
                    double[] c = T.get(index);
                    c = new double[2];
                    c[0] = 1;    //T1的语义分数Ci
                    c[1] = YUZHI;//T2的语义分数Ci
                    T.put(index, c);
                }
            }

            for (int i = 0; i < size2; i++) {
                index = T2.get(i);
                if (index != null) {
                    double[] c = T.get(index);
                    if (c != null && c.length == 2) {
                        c[1] = 1; //T2中也存在，T2的语义分数=1
                    } else {
                        c = new double[2];
                        c[0] = YUZHI; //T1的语义分数Ci
                        c[1] = 1; //T2的语义分数Ci
                        T.put(index, c);
                    }
                }
            }

            //开始计算，百分比
            Iterator<String> it = T.keySet().iterator();
            double s1 = 0, s2 = 0, Ssum = 0;  //S1、S2
            while (it.hasNext()) {
                double[] c = T.get(it.next());
                Ssum += c[0] * c[1];
                s1 += c[0] * c[0];
                s2 += c[1] * c[1];
            }
            //百分比
            return Ssum / Math.sqrt(s1 * s2);
        } else {
            throw new Exception("传入参数有问题！");
        }
    }

    public static void main(String[] args) {
        double a = getSimilarity("第一个字符和我们之间的关系", "第一个字符和最后一个字符没有汉字");
    }

}
