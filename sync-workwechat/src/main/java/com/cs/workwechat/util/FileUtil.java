package com.cs.workwechat.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * @Author: CS
 * @Date: 2020/7/13 2:12 下午
 * @Description:
 */
@Slf4j
public class FileUtil {
    /**
     * 系统版本
     */
    private static final String WINDOWS = "windows";
    private static final String LINUX = "linux";
    private static final String MAC = "mac";
    /**
     * 默认加载文件
     * 注：加载yaml文件，会使prefix失效，需要注意key值区分开来
     */
    private static final String PROPERTIES_FILE = "bootstrap.yaml";

    /**
     * 静态加载配置文件
     *
     * @return
     * @throws Exception
     */
    public static Properties loadProperties() throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = getFileInputStream(PROPERTIES_FILE);
        if (Objects.nonNull(inputStream)) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                log.error("读取配置文件：{} 出错", PROPERTIES_FILE);
                throw new IOException("读取配置文件出错");
            }
        }
        return properties;
    }

    /**
     * 读取resource下的文件流
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static InputStream getFileInputStream(String fileName) throws Exception {
        InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
//        String osName = System.getProperty("os.name").toLowerCase();

//                if (osName.contains("mac")) {
//            inputStream = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
//        } else if (osName.contains("mac")) {
//            File file = new File(fileName);
//            if (file.exists() && file.isFile() && file.canRead()) {
//                log.info("加载配置,位置:{}", file.getAbsolutePath());
//                try {
//                    inputStream = new FileInputStream(file);
//                } catch (FileNotFoundException e) {
//                    log.error("文件:{} 不存在", fileName);
//                    throw new FileNotFoundException(e.getCause().getMessage());
//                }
//            } else {
//                log.error("读配置文件 {} 错误! ", file.getPath());
//                throw new Exception("读配置文件错误! ");
//            }
//        } else {
//            log.error("不支持：{} 系统", osName);
//            throw new Exception("不支持的操作系统！");
//        }
        return inputStream;
    }
}
