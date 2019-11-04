package com.cs.auth.security.handler;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cs.auth.security.service.JwtUserService;
import com.cs.auth.security.token.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author keson
 * @date 2018/12/16 23:14
 */
public class JwtRefreshSuccessHandler implements AuthenticationSuccessHandler {
	/**
	 * 刷新间隔30分钟(单位：秒)
	 */
	private static final Long TOKEN_REFRESH_INTERVAL = 30 * 60L;

	private JwtUserService jwtUserService;

	public JwtRefreshSuccessHandler(JwtUserService jwtUserService) {
		this.jwtUserService = jwtUserService;
	}

	protected boolean shouldTokenRefresh(Date issueAt) {
		LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
		return LocalDateTime.now().minusSeconds(TOKEN_REFRESH_INTERVAL).isAfter(issueTime);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		DecodedJWT jwt = ((JwtAuthenticationToken) authentication).getToken();
		boolean shouldRefresh = shouldTokenRefresh(jwt.getIssuedAt());
		if (shouldRefresh) {
			String newToken = jwtUserService.jwtAuthorizationTokenRefresh(jwt, authentication);
			response.setHeader("Authorization", newToken);
		}
	}
}
