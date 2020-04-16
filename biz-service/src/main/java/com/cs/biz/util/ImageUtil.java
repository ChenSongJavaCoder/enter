package com.cs.biz.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * @Author: CS
 * @Date: 2020/4/16 5:27 下午
 * @Description:
 */
public class ImageUtil {
    /**
     * 字符集
     */
    private static final String CHARSET = "utf-8";
    /**
     * 格式化名称
     */
    private static final String FORMAT_NAME = "png";
    /**
     * 二维码尺寸
     */
    private static final int QRCODE_SIZE = 600;
    /**
     * LOGO宽度
     */
    private static final int WIDTH = 300;
    /**
     * LOGO高度
     */
    private static final int HEIGHT = 300;

    private static Hashtable HINTS = new Hashtable();

    static {
        // 容错等级 L、M、Q、H 其中 L 为最低, H 为最高
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 字符编码
        HINTS.put(EncodeHintType.CHARACTER_SET, CHARSET);
        // 二维码与图片边距
        HINTS.put(EncodeHintType.MARGIN, 1);
    }


    /**
     * 生成二维码图片
     *
     * @param content      内容
     * @param imgPath      插入图片路径
     * @param needCompress 是否压缩
     * @return
     * @throws Exception
     */
    private static BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, HINTS);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (StringUtils.isEmpty(imgPath)) {
            return image;
        }
        // 插入logo图片
        insertImage(image, imgPath, needCompress);
        return image;
    }

    /**
     * 二维码加入logo
     *
     * @param source       二维码
     * @param imgPath      logo图片地址
     * @param needCompress 是否需要压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println(imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        // 压缩LOGO
        if (needCompress) {
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成带logo的二维码图片
     *
     * @param content
     * @param imgPath
     * @param destPath
     * @param needCompress
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, imgPath, needCompress);
        mkdirs(destPath);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }

    /**
     * 生成不压缩带logo的二维码图片
     *
     * @param content
     * @param imgPath
     * @param destPath
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath) throws Exception {
        encode(content, imgPath, destPath, false);
    }

    /**
     * 生成普通不带logo的二维码图片
     *
     * @param content
     * @param destPath
     * @throws Exception
     */
    public static void encode(String content, String destPath) throws Exception {
        encode(content, null, destPath, false);
    }

    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
            throws Exception {
        BufferedImage image = createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    public static void encode(String content, OutputStream output) throws Exception {
        encode(content, null, output, false);
    }

    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    public static String decode(String path) throws Exception {
        return decode(new File(path));
    }


    private static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    public static void main(String[] args) throws Exception {

        encode("just kidding", "/Users/chensong/Desktop/图片/test.png");
        encode("hello world", "/Users/chensong/Desktop/图片/IMG_2245.JPG", "/Users/chensong/Desktop/图片/test.png", true);

    }
}
