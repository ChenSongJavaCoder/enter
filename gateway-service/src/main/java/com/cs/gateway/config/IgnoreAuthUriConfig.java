package com.cs.gateway.config;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: IngnoreUriConfig
 * @Author: CS
 * @Date: 2019/11/4 19:52
 * @Description:
 */
@Component
public class IgnoreAuthUriConfig {


    public static List<String> ignoreAuthUrls() {
		return Arrays.asList(
				"/auth-service/oauth/login",
				"/auth-service/logout"
		);
	}


    public static List<String> ignoreSwaggerUrlSuffix() {
        return Arrays.asList(
                "admin-server",
                "auth-service",
                "gateway-service"
        );
    }
}
