package com.cs.auth.feign;

import com.cs.user.api.TestApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName: FeignUserApi
 * @Author: CS
 * @Date: 2019/11/2 16:10
 * @Description:
 */
@FeignClient("user-service")
public interface FeignUserApi extends TestApi {
}
