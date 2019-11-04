package com.cs.auth.security.handler;

import com.cs.auth.security.service.JwtUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author keson
 * @date 2018/12/16 23:19
 */
public class TokenClearLogoutHandler implements LogoutHandler {
	private JwtUserService jwtUserService;

	public TokenClearLogoutHandler(JwtUserService jwtUserService) {
		this.jwtUserService = jwtUserService;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		clearToken(authentication);
	}

	protected void clearToken(Authentication authentication) {
		if (authentication == null) {
			return;
		}
		UserDetails user = (UserDetails) authentication.getPrincipal();
		if (user != null && user.getUsername() != null) {
			jwtUserService.deleteUserLoginInfo(user.getUsername());
		}
	}

}
