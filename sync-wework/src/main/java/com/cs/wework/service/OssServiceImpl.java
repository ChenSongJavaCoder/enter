package com.cs.wework.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.cs.wework.config.OssConfig;
import com.cs.wework.pojo.enums.MediaType;
import com.cs.wework.util.ContentTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @Author: CS
 * @Date: 2020/3/19 5:39 下午
 * @Description:
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    OssConfig ossConfig;

    String datePath = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + File.separator;

    @Override
    public String upload(File file, MediaType mediaType) {
        String key = ossConfig.getFilePath(mediaType) + datePath + file.getName();
        String ext = file.getName().substring(file.getName().lastIndexOf("."));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(ContentTypeUtil.getContentType(ext));
        objectMetadata.addUserMetadata("filename", key);

        OSSClient ossClient = ossConfig.ossClient();
        ossClient.putObject(ossConfig.getBucketName(), key, file, objectMetadata);
        ossClient.setObjectAcl(ossConfig.getBucketName(), key, CannedAccessControlList.PublicRead);
        ossClient.shutdown();
        log.info("上传key = {} 的图片,oss文件地址 = {}", key, ossConfig.getUrlPrefix() + File.separator + key);

        return key;
    }

}
