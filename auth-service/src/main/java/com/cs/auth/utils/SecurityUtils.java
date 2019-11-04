package com.cs.auth.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * @author keson
 * @date 2019/1/2 13:38
 */
public class SecurityUtils {
	public static Long currentUserId() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (StringUtils.isNotEmpty(user.getUsername())) {
			return Long.valueOf(user.getUsername());
		}
		throw new IllegalStateException("无法获取当前用户！");
	}
}
