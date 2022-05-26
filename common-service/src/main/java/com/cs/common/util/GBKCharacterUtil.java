package com.cs.common.util;

import cn.hutool.core.text.UnicodeUtil;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: CS
 * @date: 2021/7/7 下午1:39
 * @description: GBK不可见字符工具类
 * @see <p>https://blog.csdn.net/genglei01/article/details/8876485<p/>
 * @see <p>https://blog.csdn.net/archer119/article/details/52202065</p>
 * @see <p>https://blog.csdn.net/aodiyi6351/article/details/101705140</>
 * @see <p>https://www.qqxiuzi.cn/bianma/zifuji.php</p>
 * @see cn.hutool.core.text.UnicodeUtil
 * @see cn.hutool.core.util.ReUtil

 * GBK汉字范围：\u4E00-\u9FA5 (汉字范围\u4E00-\u9FFF)
 * ASCII范围：\u0000-\u007F 对应全部128个ACSII字符
 *
 *
 *
 * 特殊字符：㎡ \u33a1 属于GBK
 */
public class GBKCharacterUtil {

    /**
     * 属于GBK字符的正则表达式
     * todo 目前还不完善
     * “”㎡ 都是属于GBK的字符符号
     *
     * 类似<0xa0>为不可见字符
     */
    final static String GBK_EXCLUDE_REGEX = "[^\u4E00-\u9FA5\u3000-\u303F\uFF00-\uFFEF\u0000-\u007F\u201c-\u201d\u33a1-\u33a1]";
    final static Pattern GBK_EXCLUDE_PATTERN = Pattern.compile(GBK_EXCLUDE_REGEX);
    final static String DEFAULT_REPLACE_MARK = "";


    /**
     * 是否包含非GBK字符
     *
     * @param target 目标检测数据
     * @return true/false
     */
    public static boolean containsUnGBKCharacter(String target) {
        Matcher matcher = GBK_EXCLUDE_PATTERN.matcher(target);
        return matcher.find();
    }

    /**
     * 提取非GBK字符
     *
     * @param target
     * @return
     */
    public static List<String> extractUnGBKCharacter(String target) {
        List<String> unGBKCharacter = Lists.newArrayList();
        if (containsUnGBKCharacter(target)) {
            Matcher matcher = GBK_EXCLUDE_PATTERN.matcher(target);
            while (matcher.find()) {
                unGBKCharacter.add(matcher.group());
            }
        }
        return unGBKCharacter;
    }

    /**
     * 移除非GBK字符
     *
     * @param target
     */
    public static void removeUnGBKCharacter(String target) {
        replaceWithMark(target, DEFAULT_REPLACE_MARK);
    }

    /**
     * 非GBK字符替换成对应字符串
     *
     * @param target      目标检测数据
     * @param replaceMark 替换标记字符
     * @return 处理后的目标数据
     */
    public static String replaceWithMark(String target, String replaceMark) {
        if (containsUnGBKCharacter(target)) {
            target = target.replaceAll(GBK_EXCLUDE_REGEX, replaceMark);
        }
        return target;
    }


    public static void main(String[] args) {
        String s = UnicodeUtil.toString("\u007F");
        String target = "山西省太原市迎泽区迎泽大街269号   0351-8950351㎡  m²😀😃";
        System.out.println("是否包含GBK不可见字符：" + containsUnGBKCharacter(target));
        System.out.println("处理字符串：" + replaceWithMark(target, "'"));
    }

}
