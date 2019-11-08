package com.cs.user.feign;

import com.cs.message.api.RabbitMqApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName: FeignRabbitMqApi
 * @Author: CS
 * @Date: 2019/11/8 14:38
 * @Description:
 */
@FeignClient("message-service")
public interface FeignRabbitMqApi extends RabbitMqApi {
}
