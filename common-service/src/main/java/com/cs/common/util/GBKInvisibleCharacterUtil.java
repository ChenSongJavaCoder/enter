package com.cs.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: CS
 * @date: 2021/7/7 下午1:39
 * @description: GBK不可见字符工具类
 * @see <p>https://blog.csdn.net/genglei01/article/details/8876485<p/>
 */
public class GBKInvisibleCharacterUtil {

    /**
     * GBK不可见字符的正则表达式
     * 类似<0xa0>
     */
    final static String regex = "[^\u4E00-\u9FA5\u3000-\u303F\uFF00-\uFFEF\u0000-\u007F\u201c-\u201d]";
    final static Pattern pattern = Pattern.compile(regex);
    final static String DEFAULT_REPLACE_MARK = "";


    /**
     * 是否包含不可见字符
     *
     * @param target 目标检测数据
     * @return true/false
     */
    public static boolean containsInvisibleCharacter(String target) {
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    /**
     * 移除不可见字符
     *
     * @param target
     */
    public static void removeInvisibleCharacter(String target) {
        replaceWithMark(target, DEFAULT_REPLACE_MARK);
    }

    /**
     * 不可见字符替换成对应字符串
     *
     * @param target      目标检测数据
     * @param replaceMark 替换标记字符
     * @return 处理后的目标数据
     */
    public static String replaceWithMark(String target, String replaceMark) {
        if (containsInvisibleCharacter(target)) {
            target = target.replaceAll(regex, replaceMark);
        }
        return target;
    }


    public static void main(String[] args) {
        String target = "山西省太原市迎泽区迎泽大街269号   0351-8950351";
        System.out.println("是否包含GBK不可见字符：" + containsInvisibleCharacter(target));
        System.out.println("处理字符串：" + replaceWithMark(target, "&"));
    }

}