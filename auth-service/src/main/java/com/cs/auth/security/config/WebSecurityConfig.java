package com.cs.auth.security.config;


import com.cs.auth.security.filter.OptionsRequestFilter;
import com.cs.auth.security.handler.JsonPcLoginSuccessHandler;
import com.cs.auth.security.handler.JwtLogoutSuccessHandler;
import com.cs.auth.security.handler.JwtRefreshSuccessHandler;
import com.cs.auth.security.handler.TokenClearLogoutHandler;
import com.cs.auth.security.provider.JwtAuthenticationProvider;
import com.cs.auth.security.service.JwtUserService;
import com.cs.auth.security.service.UsernamePasswordUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * @ClassName: UserCsMapper
 * @Author: CS
 * @Date: 2019/11/2 11:02
 * @Description:
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UsernamePasswordUserService usernamePasswordUserService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.antMatchers("/swagger-ui.html").permitAll()
				.antMatchers("/swagger-resources/**").permitAll()
				.antMatchers("/images/**").permitAll()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/v2/api-docs").permitAll()
				.antMatchers("/configuration/ui").permitAll()
				.antMatchers("/configuration/security").permitAll()
				.antMatchers("/actuator/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf().disable()
				.formLogin().disable()
				.sessionManagement().disable()
				.cors()
				.and()
				.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
				new Header("Access-Control-Expose-Headers", "Authorization"))))
				.and()
				.addFilterAfter(new OptionsRequestFilter(), CorsFilter.class)
				.apply(new JsonPcLoginConfigurer<>()).loginSuccessHandler(jsonPcLoginSuccessHandler())
				.and()
				.apply(new JwtLoginConfigurer<>()).tokenValidSuccessHandler(jwtRefreshSuccessHandler()).permissiveRequestUrls("/logout")
				.and()
				.logout()
				.logoutUrl("/logout")
				.addLogoutHandler(tokenClearLogoutHandler())
				.logoutSuccessHandler(new JwtLogoutSuccessHandler())
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.headers().cacheControl();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider())
				.authenticationProvider(jwtAuthenticationProvider());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean("jwtAuthenticationProvider")
	protected AuthenticationProvider jwtAuthenticationProvider() {
		return new JwtAuthenticationProvider(jwtUserService());
	}

	@Bean("daoAuthenticationProvider")
	protected AuthenticationProvider daoAuthenticationProvider() throws Exception {
		//这里会默认使用BCryptPasswordEncoder比对加密后的密码，注意要跟createUser时保持一致
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(usernamePasswordUserService);
		return daoProvider;
	}

	@Override
	protected UserDetailsService userDetailsService() {
		return jwtUserService();
	}

	@Bean
	protected JwtUserService jwtUserService() {
		return new JwtUserService();
	}

	@Bean
	protected JsonPcLoginSuccessHandler jsonPcLoginSuccessHandler() {
		return new JsonPcLoginSuccessHandler(jwtUserService());
	}

	@Bean
	protected JwtRefreshSuccessHandler jwtRefreshSuccessHandler() {
		return new JwtRefreshSuccessHandler(jwtUserService());
	}

	@Bean
	protected TokenClearLogoutHandler tokenClearLogoutHandler() {
		return new TokenClearLogoutHandler(jwtUserService());
	}

}
