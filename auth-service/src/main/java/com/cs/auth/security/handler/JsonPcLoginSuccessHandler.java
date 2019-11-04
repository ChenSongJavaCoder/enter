package com.cs.auth.security.handler;

import com.cs.auth.security.service.JwtUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author keson
 * @date 2018/12/16 21:51
 */
public class JsonPcLoginSuccessHandler implements AuthenticationSuccessHandler {

	private JwtUserService jwtUserService;

	public JsonPcLoginSuccessHandler(JwtUserService jwtUserService) {
		this.jwtUserService = jwtUserService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	                                    Authentication authentication) throws IOException, ServletException {
		String token = jwtUserService.createPcAuthorizationToken((UserDetails) authentication.getPrincipal());
		response.setHeader("Authorization", token);
		response.setContentType("APPLICATION_JSON");
	}
}
