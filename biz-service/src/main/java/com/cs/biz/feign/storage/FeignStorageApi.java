package com.cs.biz.feign.storage;

import com.cs.storage.api.TestApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName: FeignStorageApi
 * @Author: CS
 * @Date: 2019/11/1 17:38
 * @Description:
 */
@FeignClient("storage-service")
public interface FeignStorageApi extends TestApi {
}
