package com.cs.wework.service;

import com.cs.wework.pojo.enums.MediaType;

import java.io.File;


/**
 * @Author: CS
 * @Date: 2020/3/19 5:39 下午
 * @Description:
 */
public interface OssService {

    /**
     * 上传文件
     *
     * @param file
     * @param mediaType
     * @return
     */
    String upload(File file, MediaType mediaType);


}
