package com.cs.auth.security.config;

import com.cs.auth.security.filter.UsernamePasswordAuthenticationFilter;
import com.cs.auth.security.handler.JsonLoginFailureHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

/**
 * @author keson
 * @date 2018/12/16 21:56
 */
public class JsonPcLoginConfigurer<T extends JsonPcLoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
	private UsernamePasswordAuthenticationFilter authFilter;

	public JsonPcLoginConfigurer() {
		this.authFilter = new UsernamePasswordAuthenticationFilter();
	}

	@Override
	public void configure(B http) throws Exception {
		//设置Filter使用的AuthenticationManager,这里取公共的即可
		authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		//设置失败的Handler
		authFilter.setAuthenticationFailureHandler(new JsonLoginFailureHandler());
		//不将认证后的context放入session
		authFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

		UsernamePasswordAuthenticationFilter filter = postProcess(authFilter);
		//指定Filter的位置
		http.addFilterAfter(filter, LogoutFilter.class);
	}

	public JsonPcLoginConfigurer<T, B> loginSuccessHandler(AuthenticationSuccessHandler authSuccessHandler) {
		authFilter.setAuthenticationSuccessHandler(authSuccessHandler);
		return this;
	}
}
