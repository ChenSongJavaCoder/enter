package com.cs.biz.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.ImageToPDF;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author: CS
 * @date: 2021/3/20 上午10:07
 * @description: pdf工具类，主要是使用pdfbox
 * 官方文档：https://pdfbox.apache.org/docs/2.0.13/javadocs/
 * 对于服务器上pdf转图片部分字体不支持的解决方案 https://www.jianshu.com/p/d69e8f812945
 */
public class PDFUtil {

    /**
     * 多图片合成pdf的限制后缀
     */
    private static final List IMAGE_SUFFIX = Arrays.asList("jpg", "png", "jpeg");
    private static final String IMAGE_TYPE = "png";
    private static final String PDF = ".pdf";
    private static final String PNG = ".png";

    static {
        // Important notice when using PDFBox with Java 8 before 1.8.0_191 or Java 9 before 9.0.4
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }

    static String[] arg = {
            "-autoOrientation", "-resize", "-landscape"
            , "/Users/chensong/Desktop/tmp/国章1.jpg"
            , "/Users/chensong/Desktop/tmp/国章1.pdf"
    };

    public static void main(String[] args) {
        try {
//            ExtractTextSimple.main(arg);

//            PDFToImage.main(arg);
            ImageToPDF.main(new String[]{});
//            manyImageToOnePdf("/Users/chensong/Desktop/tmp/","/Users/chensong/Desktop/tmp/merge.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多个图片合成一个pdf
     *
     * @param imgFolder 多图片的文件夹路径  例如:"D:\\image\\"
     * @param target    合并的图片路径          "D:\\image\\merge.pdf"
     * @throws IOException
     */
    public static void manyImageToOnePdf(String imgFolder, String target) throws IOException {
        PDDocument doc = new PDDocument();
        //创建一个空的pdf文件
        doc.save(target);

        PDPage page;
        PDImageXObject pdImage;
        PDPageContentStream contents;
        BufferedImage bufferedImage;
        String fileName;
        float w, h;
        String suffix;
        File tempFile;
        int index;

        File folder = new File(imgFolder);
        for (int i = 0; i < folder.listFiles().length; i++) {
            tempFile = folder.listFiles()[i];
            if (!tempFile.isFile()) {
                continue;
            }

            fileName = tempFile.getName();
            index = fileName.lastIndexOf(".");
            if (index == -1) {
                continue;
            }
            //获取文件的后缀
            suffix = fileName.substring(index + 1);
            //如果文件后缀不是图片格式,跳过当前循环
            if (!IMAGE_SUFFIX.contains(suffix)) {
                continue;
            }

            bufferedImage = ImageIO.read(folder.listFiles()[i]);
            //Retrieving the page
            pdImage = LosslessFactory.createFromImage(doc, bufferedImage);
            w = pdImage.getWidth();
            h = pdImage.getHeight();
            page = new PDPage(new PDRectangle(w, h));
            contents = new PDPageContentStream(doc, page);
            contents.drawImage(pdImage, 0, 0, w, h);
            System.out.println("Image inserted");
            contents.close();
            doc.addPage(page);
        }
        //保存pdf
        doc.save(target);
        //关闭pdf
        doc.close();
    }

    /**
     * PDF转图片
     *
     * @return 转换图片后文件的绝对路径
     */
    public static List<String> pdfToImage(String pdfPath) throws IOException {
        if (pdfPath.indexOf(PDF) < 0) {
            return Collections.emptyList();
        }
        long start = System.currentTimeMillis();
        List<String> pngPath = new ArrayList<>(8);
        String destPrefix = pdfPath.substring(0, pdfPath.lastIndexOf(PDF)).concat("-");
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < document.getNumberOfPages(); ++i) {
                String dest = destPrefix + i + PNG;
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, 200);
                ImageIO.write(bufferedImage, IMAGE_TYPE, new File(dest));
                pngPath.add(dest);
            }
        }
        return pngPath;
    }

}
