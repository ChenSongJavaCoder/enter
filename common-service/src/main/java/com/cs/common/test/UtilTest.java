package com.cs.common.test;


import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.WatchUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.concurrent.Executors;

/**
 * @Author: CS
 * @Date: 2020/7/13 5:17 下午
 * @Description:
 */
public class UtilTest {

    public static void main(String[] args) throws IOException {
        // 获取屏幕高度
//        int i = ScreenUtil.getHeight();
//
//        // 验证身份证号码
//        String idCard = "342426199308142010";
//        boolean card = IdcardUtil.isValidCard(idCard);
//        int age = IdcardUtil.getAgeByIdCard(idCard);
//        String prov = IdcardUtil.getProvinceByIdCard(idCard);
//
//        // shell命令
//        String res = RuntimeUtil.execForStr("pwd");
//
//        String unicode = UnicodeUtil.toUnicode("我是一只小小鸟");
//
//        boolean ping = NetUtil.ping("61.135.169.125", 1000);
//        NetUtil.getIpByHost("www.baidu.com");
//
//        ChineseDate chineseDate = new ChineseDate(new Date());
//        String chineseDay = chineseDate.getChineseDay();
//        String zodiac = Zodiac.getZodiac(new Date());

//        List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
//        for (GarbageCollectorMXBean bean : beans) {
//            System.out.println(bean.getName());
//        }

//        System.out.println(SystemUtil.getHostInfo().getName());
//        ThreadMXBean info = SystemUtil.getThreadMXBean();

//        List<File> files = FileUtil.loopFiles("/Users/chensong/Downloads/fpxls");
//        files.stream().filter(f -> f.getName().contains("91350203079382792K-20210527181665800-201610-进项票-增值税普通发票-1.xls")).forEach(e -> {
//            ExcelReader reader = ExcelUtil.getReader(e, 1);
//            int count = reader.getRowCount();
//            Row row = reader.getSheet().getRow(3);
//        });

//        System.out.println(files.size());
        // 获取系统cpu数量
//        System.out.println(Runtime.getRuntime().availableProcessors());
//        System.out.println(System.getProperty("java.io.tmpdir"));


//        String downloadUrl = "http://rys-ops.oss-cn-hangzhou.aliyuncs.com/20220126/9d1be909b9c2a955e3cfb7eba29e7b2f/cloud-gateway.jar?Expires=1643190860&OSSAccessKeyId=LTAI4GJ5JaeXNsQhEbZXPSFD&Signature=s1tZ%2FZoEDARwG71%2Bxj%2BCa5%2BQabc%3D";
//        String dest = "/Users/chensong/Study/";
//        // 网络链接下载文件到指定文件夹
//        HttpUtil.downloadFile(downloadUrl, dest);

        // 验证码
//        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(200, 200);
//        System.out.println(circleCaptcha.getImageBase64Data());
//                ImageIO.write(circleCaptcha.getImage(),"test.png",new File("/Users/chensong/Study/"));


//        String join = String.join(",", Arrays.asList("a", "b", "c"));
//        System.out.println(join);

        // 监听文件，会阻塞当前线程，如需不影响主线程可使用异步线程处理
        WatchMonitor watchMonitor = WatchUtil.createModify(new File("/Users/chensong/Desktop/其他"), new MyWatcher());
//        watchMonitor.watch();
        Executors.newSingleThreadExecutor().execute(watchMonitor);

        System.out.println("初始化完成");
    }

    public static class MyWatcher extends SimpleWatcher {

        @Override
        public void onModify(WatchEvent<?> event, Path currentPath) {
            System.out.println(event.kind() + currentPath.toString());
        }
    }

}
