package com.cs.biz.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author: CS
 * @date: 2021/11/24 上午10:41
 * @description: 文件压缩工具类
 */
@Slf4j
public class ZipUtil {

    private static final int BUFFER_BYTE_SIZE = 1024;

    private ZipUtil() {
    }

    public static File compress(List<File> sourceFiles, String zipFileName) throws IOException {
        byte[] buf = new byte[BUFFER_BYTE_SIZE];
        ZipOutputStream zos = null;
        FileInputStream fis = null;
        try {
            File zipFile = File.createTempFile(zipFileName, ".zip");
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for (File sourceFile : sourceFiles) {
                if (sourceFile == null || !sourceFile.exists()) {
                    continue;
                }
                fis = new FileInputStream(sourceFile);
                //直接放到压缩包的根目录
                String originalFilename = sourceFile.getName();
                ZipEntry zipEntry = new ZipEntry(originalFilename);
                zos.putNextEntry(zipEntry);
                int len;
                while ((len = fis.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
            }
            return zipFile;
        } finally {
            if (null != zos) {
                try {
                    zos.close();
                } catch (IOException e) {
                    log.error("io close error:", e);
                }
            }
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("io close error:", e);
                }
            }
        }
    }
}
