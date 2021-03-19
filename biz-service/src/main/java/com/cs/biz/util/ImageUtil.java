package com.cs.biz.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Objects;

/**
 * @Author: CS
 * @Date: 2020/4/16 5:27 下午
 * @Description:：图片转化工具类
 */
public class ImageUtil {
    /**
     * 字符集
     */
    private static final String CHARSET = Charset.defaultCharset().name();
    /**
     * 格式化名称
     */
    private static final String FORMAT_NAME = "png";
    /**
     * 二维码尺寸
     */
    private static final int QRCODE_SIZE = 300;
    /**
     * LOGO宽度
     */
    private static final int WIDTH = 90;
    /**
     * LOGO高度
     */
    private static final int HEIGHT = 90;

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

    public static String encodeBase64(String content, String destPath) throws Exception {
        return encodeBase64(content, destPath, true);
    }

    public static String encodeBase64(String content, String destPath, boolean deleteFile) throws Exception {
        encode(content, destPath);
        return imageToBase64(destPath, deleteFile);
    }

    public static String imageToBase64(String path, boolean deleteFile) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(path);
            System.out.println("文件大小（字节）=" + in.available());
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组进行Base64编码，得到Base64编码的字符串
        String base64Str = Base64.getEncoder().encodeToString(data);
        ;
        if (deleteFile) {
            File file = new File(path);
            if (Objects.nonNull(file) && file.exists()) {
                file.delete();
            }
        }
        return base64Str;
    }

    @SneakyThrows
    public static boolean decodeBase64(String imgStr, String imgFilePath) {
        OutputStream out = null;
        try {
            // Base64解码
            byte[] bytes = Base64.getDecoder().decode(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                // 调整异常数据
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (Objects.nonNull(out)) {
                out.flush();
                out.close();
            }
        }
    }

    private static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    public static void main(String[] args) throws Exception {

//        encode("oh, sweetheart", "/Users/chensong/Desktop/WechatIMG1.jpeg");
//        encode("https://www.bilibili.com/video/BV1jE41137PK?from=search&seid=6511489059997668968", "/Users/chensong/Desktop/1594117789245.jpg", "/Users/chensong/Desktop/test4.png", true);
//        String s = "PHN2ZyB3aWR0aD0iMzAwcHgiIGhlaWdodD0iMTAwcHgiIHZpZXdCb3g9IjAgMCAzMDBweCAxMDBweCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj4KICAgICAgICAgICAgICAgIDx0aXRsZT7pobXpnaIgMTwvdGl0bGU+CiAgICAgICAgICAgICAgICA8ZGVmcz4KICAgICAgICAgICAgICAgICAgICA8cG9seWdvbiBpZD0icGF0aC0xIiBwb2ludHM9IjAuMDc5NjY0NzE2IDAuMDA4ODU3MTQyODYgNC42MjYyMTg5MyAwLjAwODg1NzE0Mjg2IDQuNjI2MjE4OTMgNi44OTIyIDAuMDc5NjY0NzE2IDYuODkyMiI+PC9wb2x5Z29uPgogICAgICAgICAgICAgICAgICAgIDxwb2x5Z29uIGlkPSJwYXRoLTMiIHBvaW50cz0iMC4wMDk0OTE1NjU3IDAuMDY5NDU3OTUyNCAxNi40MjU5MjU0IDAuMDY5NDU3OTUyNCAxNi40MjU5MjU0IDEyLjQ0NDQ0NDQgMC4wMDk0OTE1NjU3IDEyLjQ0NDQ0NDQiPjwvcG9seWdvbj4KICAgICAgICAgICAgICAgICAgICA8cG9seWdvbiBpZD0icGF0aC01IiBwb2ludHM9IjAgMjEgNjUgMjEgNjUgMCAwIDAiPjwvcG9seWdvbj4KICAgICAgICAgICAgICAgIDwvZGVmcz4KICAgICAgICAgICAgICAgIDxnIGlkPSLpobXpnaItMSIgc3Ryb2tlPSJub25lIiBzdHJva2Utd2lkdGg9IjEiIGZpbGw9Im5vbmUiIGZpbGwtcnVsZT0iZXZlbm9kZCI+CiAgICAgICAgICAgICAgICAgICAgPGcgaWQ9InrlpIfku70tMiIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoLTc2NS4wMDAwMDAsIC02MjQuMDAwMDAwKSI+CiAgICAgICAgICAgICAgICAgICAgICAgIDx0ZXh0IG9wYWNpdHk9IjAuMjUiIGZvbnQtZmFtaWx5PSJQaW5nRmFuZ1NDLVNlbWlib2xkLCBQaW5nRmFuZyBTQyIgZm9udC1zaXplPSIxNiIgZm9udC13ZWlnaHQ9IjUwMCIgbGluZS1zcGFjaW5nPSIxNiIgZmlsbD0iIzAwMDAwMCI+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8dHNwYW4gdGV4dC1hbmNob3I9ImVuZCIgeD0iMTA2NiIgeT0iNjQwIj5A5rWp5aeQNTIwPC90c3Bhbj4KICAgICAgICAgICAgICAgICAgICAgICAgPC90ZXh0PgogICAgICAgICAgICAgICAgICAgICAgICA8ZyBpZD0i57yW57uEIiBvcGFjaXR5PSIwLjIwMDAwMDAwMyIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMTAwMC4wMDAwMDAsIDY0My4wMDAwMDApIj4KICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxnPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik00Mi4yODA1NjUsOC44ODkzMTk3MSBDNDIuMjgwNTY1LDguMjIzMTYyOTEgNDEuNzU0Nzg0NCw2LjIyMjIyMjIyIDM5LjgwNDcwNDIsNi4yMjIyMjIyMiBDMzcuODUyMDEwMiw2LjIyMjIyMjIyIDM3LjU5MDM2MTQsOC4xMTAzNTI2OCAzNy41OTAzNjE0LDkuNDQ1Njg1NTMgQzM3LjU5MDM2MTQsMTAuNzIxMTgyMyAzNy42OTQwMDE1LDEyLjQ5OTkzMzUgNDAuMTIwNzIxNSwxMi40NDMxMTY2IEM0Mi41NDYzOTU5LDEyLjM4Nzk0NjcgNDIuMjgwNTY1LDkuNTU1MjAyMDMgNDIuMjgwNTY1LDguODg5MzE5NzEiIGlkPSJGaWxsLTEiIGZpbGw9IiMwMTAyMDIiPjwvcGF0aD4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZyB0cmFuc2Zvcm09InRyYW5zbGF0ZSgyNy40MDk2MzksIDAuMDAwMDAwKSI+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxtYXNrIGlkPSJtYXNrLTIiIGZpbGw9IndoaXRlIj4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDx1c2UgeGxpbms6aHJlZj0iI3BhdGgtMSI+PC91c2U+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvbWFzaz4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGcgaWQ9IkNsaXAtNCI+PC9nPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cGF0aCBkPSJNMi4zNTI4MDc1Nyw2Ljg5MjIgQzMuNjA3OTIyODksNi44OTIyIDQuNjI2MjE4OTMsNS4zNTIwNTcxNCA0LjYyNjIxODkzLDMuNDUxMDU3MTQgQzQuNjI2MjE4OTMsMS41NDgyIDMuNjA3OTIyODksMC4wMDg3NzE0Mjg1NyAyLjM1MjgwNzU3LDAuMDA4NzcxNDI4NTcgQzEuMDk2NjE4MjQsMC4wMDg3NzE0Mjg1NyAwLjA3OTY2NDcxNiwxLjU0ODIgMC4wNzk2NjQ3MTYsMy40NTEwNTcxNCBDMC4wNzk2NjQ3MTYsNS4zNTIwNTcxNCAxLjA5NjYxODI0LDYuODkyMiAyLjM1MjgwNzU3LDYuODkyMiIgaWQ9IkZpbGwtMyIgZmlsbD0iIzAxMDIwMiIgbWFzaz0idXJsKCNtYXNrLTIpIj48L3BhdGg+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9nPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik0yNS41MjI3NDM4LDExLjYwNzM0MTEgQzI3LjcyNDMwMjYsMTEuMTIwNjMyNiAyNy40MjU3NzczLDguNDE4MTkzMTggMjcuMzU3ODk1Myw3LjgyNzU4MDU5IEMyNy4yNTIxMTM1LDYuOTE2MDUyNDcgMjYuMjA4NTczMSw1LjMyMzA3OTA3IDI0Ljc5MjkxNDUsNS40NTE3OTIwNyBDMjMuMDExNzU3MSw1LjYxMzk4Mzc4IDIyLjc1MjA0MDIsOC4yNTkzMzYgMjIuNzUyMDQwMiw4LjI1OTMzNiBDMjIuNTA5MzI2Miw5LjQ4MDU3NTYgMjMuMzI3MTU1NiwxMi4wOTIzMTU3IDI1LjUyMjc0MzgsMTEuNjA3MzQxMSIgaWQ9IkZpbGwtNSIgZmlsbD0iIzAxMDIwMiI+PC9wYXRoPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik0yOC4zMjk4Njk5LDE2LjQxODY1NDYgQzI4LjI2NTA4NTMsMTYuNjIyODI0IDI4LjExNDIzNzUsMTcuMTQ1MDY3NyAyOC4yNDM1MzU2LDE3LjU5ODk2ODQgQzI4LjUwMTg2MDcsMTguNjIwODE4MSAyOS4zMzkzMTY3LDE4LjY2NjY2NjcgMjkuMzM5MzE2NywxOC42NjY2NjY3IEwzMC41NDIxNjg3LDE4LjY2NjY2NjcgTDMwLjU0MjE2ODcsMTUuNTU1NTU1NiBMMjkuMjUzNTI0NiwxNS41NTU1NTU2IEMyOC42NzMzMDk1LDE1LjczNzM3MzcgMjguMzkzOTc2OCwxNi4yMTQxOTg3IDI4LjMyOTg2OTksMTYuNDE4NjU0NiIgaWQ9IkZpbGwtNyIgZmlsbD0iIzAxMDIwMiI+PC9wYXRoPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik0zNS44MTg5OTU4LDcuNzU5NTEzOTggQzM3LjY4NDM4NzcsNy45Nzg5OTQxOSAzOC44ODUxMjM3LDYuMTc0NTI5MjcgMzkuMTI1NDIwMSw0LjgwNzY3NDEyIEMzOS4zNjc1MDYzLDMuNDQxODk5NDggMzguMTYyNzQzLDEuODU0MzQ4NTQgMzYuODQwNTkxLDEuNTgyMDU4MDMgQzM1LjUxNTc1NCwxLjMwNzQ3MTQxIDMzLjg2MTQyMzIsMy4yMjg0OTcxOSAzMy43MTMxNTg0LDQuNDc5ODcyIEMzMy41MzE2MzEsNi4wMTE3NzYyNiAzMy45NTM3NTMxLDcuNTQwNDM4OTggMzUuODE4OTk1OCw3Ljc1OTUxMzk4IiBpZD0iRmlsbC05IiBmaWxsPSIjMDEwMjAyIj48L3BhdGg+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGcgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMjQuMjc3MTA4LCA4LjU1NTU1NikiPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8bWFzayBpZD0ibWFzay00IiBmaWxsPSJ3aGl0ZSI+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8dXNlIHhsaW5rOmhyZWY9IiNwYXRoLTMiPjwvdXNlPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L21hc2s+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxnIGlkPSJDbGlwLTEyIj48L2c+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik0xMy4wNDM5OTI5LDEwLjUzNDE0MTkgTDkuNjUzNzU0OTIsMTAuNTM0MTQxOSBDOC4zMzk0MjkzMywxMC4xOTUxOTgxIDguMjc2NzIxNDIsOS4yNjIxMzIzOCA4LjI3NjcyMTQyLDkuMjYyMTMyMzggTDguMjc2NzIxNDIsNS41MDc3OTA2OCBMOS42NTM3NTQ5Miw1LjQ4Njc4MjMyIEw5LjY1Mzc1NDkyLDguODU5MDkyOTUgQzkuNzM4NTI0MjQsOS4yMTkzMTI3OCAxMC4xODM0OTYzLDkuMjgzMDA2OTMgMTAuMTgzNDk2Myw5LjI4MzAwNjkzIEwxMS41ODIzMjM4LDkuMjgzMDA2OTMgTDExLjU4MjMyMzgsNS41MDc3OTA2OCBMMTMuMDQzOTkyOSw1LjUwNzc5MDY4IEwxMy4wNDM5OTI5LDEwLjUzNDE0MTkgWiBNNy41NTU5MTQ3OCwxMC41NTU4MTk0IEw0LjYwOTcxMjksMTAuNTU1ODE5NCBDMy4zMzgwMzkzOCwxMC4zMDA3NzUxIDIuODI5Mjg5NzQsOS40MzIwNzI2NCAyLjc2NTY0NTksOS4yODMwMDY5MyBDMi43MDI4MDQyOSw5LjEzNTE0NTUyIDIuMzQxOTMzLDguNDM0MjQyMjkgMi41MzI4NjQ1Myw3LjI0NzMzNjY4IEMzLjA4NDM5OTk0LDUuNDY1MzcyNTIgNC42NTE5NjM4Niw1LjMzNzg1MDQyIDQuNjUxOTYzODYsNS4zMzc4NTA0MiBMNi4yMjA5OTg1Myw1LjMzNzg1MDQyIEw2LjIyMDk5ODUzLDMuNDA4NDI2MjggTDcuNTU1OTE0NzgsMy40Mjk0MzQ2NSBMNy41NTU5MTQ3OCwxMC41NTU4MTk0IFogTTE1LjMxMDgzNjksNi4yMDI1Mzg1OSBDMTUuMzEwODM2OSw2LjIwMjUzODU5IDEyLjcyMjQzMTEsNC4xOTc2NDQ5MiAxMS4yMTIzNjA2LDIuMDMyMTc3NzggQzkuMTYxOTg1ODksLTEuMTYyODMyOTcgNi4yNTA0MTM3NSwwLjEzNjg3NTUwOCA1LjI3ODc3NTQ5LDEuNzYxMzQzODUgQzQuMzA5Njc3NjQsMy4zODU4MTIxOSAyLjc5ODkzODU4LDQuNDE0ODIwNTUgMi41ODMwMDQxMSw0LjY4NTkyMjEgQzIuMzY3MzM3MDYsNC45NTUxNTAzIC0wLjU0MzI5OTE0OCw2LjUyNTY5MjcxIDAuMTAyODk5Nzk0LDkuMzk3ODE2OTcgQzAuNzQ4MDI5MDkyLDEyLjI2NzY2NjQgMy4wMTU4MDg5OSwxMi4yMTM2MDY3IDMuMDE1ODA4OTksMTIuMjEzNjA2NyBDMy4wMTU4MDg5OSwxMi4yMTM2MDY3IDQuNjg3MjYyMTIsMTIuMzc3MTI0IDYuNjI5MjAxNTksMTEuOTQyMTAzNyBDOC41Njc5MzIxMiwxMS41MTAxNjExIDEwLjI0MDE4NzUsMTIuMDUwNzU4NCAxMC4yNDAxODc1LDEyLjA1MDc1ODQgQzEwLjI0MDE4NzUsMTIuMDUwNzU4NCAxNC43NzMyMDY5LDEzLjU2ODQ0NTQgMTYuMDEyNTIzNywxMC42NDQxMzQ4IEMxNy4yNTE0MzkzLDcuNzIwMjI1NTcgMTUuMzEwODM2OSw2LjIwMjUzODU5IDE1LjMxMDgzNjksNi4yMDI1Mzg1OSBMMTUuMzEwODM2OSw2LjIwMjUzODU5IFoiIGlkPSJGaWxsLTExIiBmaWxsPSIjMDEwMjAyIiBtYXNrPSJ1cmwoI21hc2stNCkiPjwvcGF0aD4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2c+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPG1hc2sgaWQ9Im1hc2stNiIgZmlsbD0id2hpdGUiPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8dXNlIHhsaW5rOmhyZWY9IiNwYXRoLTUiPjwvdXNlPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvbWFzaz4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZyBpZD0iQ2xpcC0xNCI+PC9nPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik0xOS41NzgzMTMzLDIwLjIyMjIyMjIgTDIxLjkyNzcxMDgsMjAuMjIyMjIyMiBMMjEuOTI3NzEwOCwxMi44MjA1MDg1IEwxOS41NzgzMTMzLDEyLjgyMDUwODUgTDE5LjU3ODMxMzMsMjAuMjIyMjIyMiBaIE0xOS41NzgzMTMzLDExLjk5NTg5ODMgTDIxLjkyNzcxMDgsMTEuOTk1ODk4MyBMMjEuOTI3NzEwOCwxMC4xMTExMTExIEwxOS41NzgzMTMzLDEwLjExMTExMTEgTDE5LjU3ODMxMzMsMTEuOTk1ODk4MyBaIiBpZD0iRmlsbC0xMyIgZmlsbD0iIzAwMDAwMCIgbWFzaz0idXJsKCNtYXNrLTYpIj48L3BhdGg+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHBhdGggZD0iTTE2LjQ2NDI0MjcsMTguNjg4NzMgTDEzLjE1MzE5NjEsMTguNjg4NzMgQzEyLjcwNzU4ODIsMTguNTI0Mzc1NSAxMi41NTkyNDI4LDE4LjI1MDIzMTYgMTIuNTI5MzczNiwxOC4xODE4Mjc0IEMxMi40OTk3OTAzLDE4LjExMzk1MDUgMTIuMzk1NzQ4NCwxNy44NjczNTI4IDEyLjUxNDIyNDcsMTcuNjIwNDkxNSBDMTIuNzgxNjE4LDE3LjE0MTY2MjMgMTMuMjI3NTExNywxNy4wMzIwMDQ4IDEzLjIyNzUxMTcsMTcuMDMyMDA0OCBMMTYuNDY0MjQyNywxNy4wMzIwMDQ4IEwxNi40NjQyNDI3LDE4LjY4ODczIFogTTE2LjAxODc3NzgsMTIuNDU3ODg4IEwxMC44MzY0MDY3LDEyLjQ0NDQ0NDQgTDEwLjgzNjQwNjcsMTQuMDczNzU1MiBMMTUuMzc5ODA2NCwxNC4wNzM3NTUyIEMxNS4zNzk4MDY0LDE0LjA3Mzc1NTIgMTYuNDY0MjQyNywxNC4zMjAwODkzIDE2LjQ2NDI0MjcsMTQuOTUwMDkzIEwxNi40NjQyNDI3LDE1LjUyNTM5OTcgTDEyLjM1MTAxNjEsMTUuNTI1Mzk5NyBDMTIuMzUxMDE2MSwxNS41MjUzOTk3IDEwLjczMjc5MzUsMTUuNjYyNzM1MyAxMC4yMTI1ODQyLDE3LjQ0MjI5OCBDMTAuMTIzODM0MiwxOC4yNzg0MzY4IDEwLjI0MjQ1MzQsMTguNjg4NzMgMTAuMzAxOTA1OSwxOC44Mzk3NzI3IEMxMC4zNjEzNTgzLDE4Ljk5MDI4ODIgMTAuODM2NDA2NywxOS45NzU2MjQ1IDEyLjI0NjgzMTQsMjAuMjIyMjIyMiBMMTguNzk1MTgwNywyMC4yMjIyMjIyIEwxOC43OTUxODA3LDE0LjY3NjA4MDkgQzE4Ljc5NTE4MDcsMTQuNjc2MDgwOSAxOC40ODM3Njk3LDEyLjc3MjM2MjcgMTYuMDE4Nzc3OCwxMi40NTc4ODggTDE2LjAxODc3NzgsMTIuNDU3ODg4IFoiIGlkPSJGaWxsLTE1IiBmaWxsPSIjMDAwMDAwIiBtYXNrPSJ1cmwoI21hc2stNikiPjwvcGF0aD4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cGF0aCBkPSJNNy4yOTA4MzkyLDEzLjY1NTAxMjMgQzcuMjkwODM5MiwxMy42NTUwMTIzIDcuMDIyOTg1NDEsMTQuMDg0MzU4MyA2LjMyMjYxODI0LDE0LjE3NTcyNDMgTDIuNTA4MDk4MzEsMTQuMTc1NzI0MyBMMi41MDgwOTgzMSwxMi4wODI4NDg2IEw2LjMwMzAyMjYyLDEyLjA4Mjg0ODYgQzYuMzAzMDIyNjIsMTIuMDgyODQ4NiA3LjEzMDY5MDgzLDEyLjA1MjE0NTYgNy40MDI0OTE5NCwxMi42MjY1ODc3IEM3LjQwMjQ5MTk0LDEyLjYyNjU4NzcgNy41NjU2MDA4LDEzLjIzNTU3MDYgNy4yOTA4MzkyLDEzLjY1NTAxMjMgTTYuNDUzNTg0NjQsMTguMzQzNDAwNSBMNi40NTM1ODQ2NCwxOC4zNDYzNzE3IEwyLjUwODA5ODMxLDE4LjM0NjM3MTcgTDIuNTA4MDk4MzEsMTYuMTY3NTc3NCBMNi40MzI0MzgyOSwxNi4xNjc1Nzc0IEw2LjU2NTA5NjQxLDE2LjE4OTI0MjcgQzYuNTY1MDk2NDEsMTYuMTg5MjQyNyA3LjMxMzgxODI0LDE2LjMxMDY5MjcgNy41ODUxOTY0MiwxNi45MDEzNTI4IEM3LjU4NTE5NjQyLDE2LjkwMTM1MjggNy43NDIyNDMzMywxNy40NTMxMzkxIDcuNDAyNDkxOTQsMTcuODY2MzkwNyBDNy40MDI0OTE5NCwxNy44NjYzOTA3IDcuMTUzNTI4ODksMTguMjQ5MzEwOCA2LjQ1MzU4NDY0LDE4LjM0MzQwMDUgTTEwLjE3NjMyOTQsMTcuNTU3MzgwNSBDMTAuMjQ1ODMwNCwxNS43ODQ1MzM0IDguNDM2MjY2NiwxNS4xNzM2OTM1IDguNDM2MjY2NiwxNS4xNzM2OTM1IEM5Ljk2NjU1NzYsMTQuNDcxNDg3NiA5Ljc3MjAxMTE2LDEyLjY5ODc2NDQgOS43NzIwMTExNiwxMi42OTg3NjQ0IEM5LjYwNDY3MzAzLDkuOTg1MDIwODEgNS44MzIzMDQ4MywxMC4xMTIwNDE4IDUuODMyMzA0ODMsMTAuMTEyMDQxOCBMMCwxMC4xMTIwNDE4IEwwLDIwLjIyMjIyMjIgTDYuNTg0NTUxMDUsMjAuMjIyMjIyMiBDMTAuNDU0NDc0NCwyMC4yMTAzMzcyIDEwLjE3NjMyOTQsMTcuNTU3MzgwNSAxMC4xNzYzMjk0LDE3LjU1NzM4MDUiIGlkPSJGaWxsLTE2IiBmaWxsPSIjMDAwMDAwIiBtYXNrPSJ1cmwoI21hc2stNikiPjwvcGF0aD4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cGF0aCBkPSJNNTMuNDEzNjc4MSwxMi42MDIyMTg4IEw0OS44NzA3OTQ4LDE2LjM2MDc2MjcgTDUyLjE2ODg4MTMsMTguNjMzMjk1NCBDNTIuMjY0NzcxMSwxOC44MzczMjM1IDUyLjU1MTc1OTUsMTguOTEwNDMwNSA1My4wMzA2NjM3LDE4Ljg1MTc0NiBMNTQuMDM2MTQ0NiwxOC44NTE3NDYgTDU0LjAzNjE0NDYsMjAuMTYyOTQ2OSBMNTEuODgxNzU2NywyMC4xNjI5NDY5IEM1MS4yNzQ5NTQsMjAuMTkxNjY3NSA1MC44OTIwNzU4LDIwLjA4OTg0IDUwLjczMjU3NzIsMTkuODU3MDkxMiBMNDguNDgyMjk5NSwxNy42NzE3MTQ5IEw0Ni43MTA5MjYsMTkuMzc2MzAxIEM0Ni4xMzY0MDQ0LDIwLjAxNjg1NzMgNDUuNjA5NTU1MywyMC4yOTM5OTI0IDQ1LjEzMDkyMzUsMjAuMjA2NDYzIEw0My4xNjc5MDY2LDIwLjIwNjQ2MyBMNDMuMTY3OTA2NiwxOC44NTE3NDYgTDQ0LjQ2MDc4NDUsMTguODUxNzQ2IEM0NC42NTIxNTU1LDE4Ljg4MTIxMjYgNDQuODQzNzk4OSwxOC43OTM4MDc1IDQ1LjAzNTAzMzcsMTguNTg5NTMwNyBMNDcuMjM3NTAyNywxNi40OTE4MDgyIEw0My44MzgxODE4LDEzLjAzOTI0NDMgTDQ2LjI3OTgzMDUsMTMuMDM5MjQ0MyBMNDguNTc3OTE2OSwxNS4xODA3MzE2IEw1MS4xMTU1OTE2LDEyLjYwMjIxODggTDUzLjQxMzY3ODEsMTIuNjAyMjE4OCBaIE00Ny40NzY4MTg2LDEwLjg5Nzg4MTQgTDQ3LjQ3NjgxODYsMTAuMTExMTExMSBMNDkuNTgzNjcwMiwxMC4xMTExMTExIEw0OS41ODM2NzAyLDEwLjg5Nzg4MTQgTDUzLjg5MjU4MjMsMTAuODk3ODgxNCBMNTMuODkyNTgyMywxMi4zNDAyNTIxIEw0My4wNzIyODkyLDEyLjM0MDI1MjEgTDQzLjA3MjI4OTIsMTAuODk3ODgxNCBMNDcuNDc2ODE4NiwxMC44OTc4ODE0IFoiIGlkPSJGaWxsLTE3IiBmaWxsPSIjMDAwMDAwIiBtYXNrPSJ1cmwoI21hc2stNikiPjwvcGF0aD4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cG9seWdvbiBpZD0iRmlsbC0xOCIgZmlsbD0iIzAwMDAwMCIgbWFzaz0idXJsKCNtYXNrLTYpIiBwb2ludHM9IjU0LjAzNjE0NDYgMTAuNDIwMjQ1MyA1OC44MTQxMjM3IDEwLjQyMDI0NTMgNTguODE0MTIzNyAxMC4xMTExMTExIDYxLjEyNzU2NzYgMTAuMTExMTExMSA2MS4xMjc1Njc2IDEwLjQyMDI0NTMgNjQuODk5NTU4NiAxMC40MjAyNDUzIDY0Ljg5OTU1ODYgMTEuNzQ0ODM2NyA2MC40MjMzMzI5IDExLjc0NDgzNjcgNjAuMDcxMjE1NiAxMi40NTE0MTExIDY0Ljg5OTU1ODYgMTIuNDUxNDExMSA2NC44OTk1NTg2IDEzLjg2NDMwODcgNTkuMjY2NjgyNSAxMy44NjQzMDg3IDU4LjQxMTY0MjYgMTUuMzY1Mzg2OCA2MC4zMjI3NDg0IDE1LjM2NTM4NjggNjAuMzIyNzQ4NCAxNC4zOTQwMTk3IDYyLjE4MzQ5MDQgMTQuMzk0MDE5NyA2Mi4xODM0OTA0IDE1LjM2NTM4NjggNjUgMTUuMzY1Mzg2OCA2NSAxNi43MzQzMTk3IDYyLjE4MzQ5MDQgMTYuNzM0MzE5NyA2Mi4xODM0OTA0IDE3LjYxNzI1NTEgNjQuOTQ5NjM2MiAxNy42MTcyNTUxIDY0Ljk0OTYzNjIgMTguODk3NzU2NCA2Mi4xODM0OTA0IDE4Ljg5Nzc1NjQgNjIuMTgzNDkwNCAyMC4yMjIyMjIyIDYwLjMyMjc0ODQgMjAuMjIyMjIyMiA2MC4zMjI3NDg0IDE4Ljg5Nzc1NjQgNTYuMzk5OTUyMyAxOC44OTc3NTY0IDU2LjM5OTk1MjMgMTcuNjE3MjU1MSA2MC4zMjI3NDg0IDE3LjYxNzI1NTEgNjAuMzIyNzQ4NCAxNi43MzQzMTk3IDU2LjM5OTk1MjMgMTYuNzM0MzE5NyA1Ni4zOTk5NTIzIDE1LjQ5ODAzNDQgNTcuMzA1MzU2IDEzLjg2NDMwODcgNTYuNDUwMTczIDEzLjg2NDMwODcgNTYuNDUwMTczIDEyLjQ1MTQxMTEgNTguMDU5NTI1MyAxMi40NTE0MTExIDU4LjQ2MTg2MzMgMTEuNzQ0ODM2NyA1NS43OTY1ODgyIDExLjc0NDgzNjcgNTUuNzk2NTg4MiAxOS4yMDY3NjQ5IDU1Ljc5NjU4ODIgMjAuMTM0MTY3MyA1NC4wMzYxNDQ2IDIwLjEzNDE2NzMgNTQuMDM2MTQ0NiAxOC41ODg2MjIyIj48L3BvbHlnb24+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2c+CiAgICAgICAgICAgICAgICAgICAgICAgIDwvZz4KICAgICAgICAgICAgICAgICAgICA8L2c+CiAgICAgICAgICAgICAgICA8L2c+CiAgICAgICAgICAgIDwvc3ZnPg==";
        String s = "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";
        decodeBase64(s, "/Users/chensong/Desktop/1.jpeg");
    }
}
