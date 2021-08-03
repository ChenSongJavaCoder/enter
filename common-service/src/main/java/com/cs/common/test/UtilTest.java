package com.cs.common.test;


/**
 * @Author: CS
 * @Date: 2020/7/13 5:17 下午
 * @Description:
 */
public class UtilTest {

    public static void main(String[] args) {
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
        System.out.println(Runtime.getRuntime().availableProcessors());
        System.out.println(System.getProperty("java.io.tmpdir"));
    }
}
