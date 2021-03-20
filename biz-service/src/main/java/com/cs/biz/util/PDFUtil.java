package com.cs.biz.util;

import org.apache.pdfbox.tools.PDFToImage;

import java.io.IOException;

/**
 * @author: CS
 * @date: 2021/3/20 上午10:07
 * @description: pdf工具类，主要是使用pdfbox
 * 官方文档：https://pdfbox.apache.org/docs/2.0.13/javadocs/
 * 对于服务器上pdf转图片部分字体不支持的解决方案 https://www.jianshu.com/p/d69e8f812945
 */
public class PDFUtil {

    static String[] arg = {"/Users/chensong/Desktop/tmp/国章.pdf", "-format", "png"};

    static {
        // Important notice when using PDFBox with Java 8 before 1.8.0_191 or Java 9 before 9.0.4
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }

    public static void main(String[] args) {
        try {
//            ExtractTextSimple.main(arg);

            PDFToImage.main(arg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
