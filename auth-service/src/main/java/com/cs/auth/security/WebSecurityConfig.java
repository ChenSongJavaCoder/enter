package com.cs.auth.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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

//	@Autowired
//	private UserService userService;
//
//
//	/**
//	 * 注入用户信息服务
//	 * @return 用户信息服务对象
//	 */
//	@Bean
//	@Override
//	public UserDetailsService userDetailsService() {
//		return userService;
//	}
//
//	/**
//	 * 全局用户信息
//	 * @param auth 认证管理
//	 * @throws Exception 用户认证异常信息
//	 */
//	@Autowired
//	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
//		//auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder());
//		auth.userDetailsService(userDetailsService())
//				.passwordEncoder(passwordEncoder());
//	}
//
//	/**
//	 * 认证管理
//	 * @return 认证管理对象
//	 * @throws Exception 认证异常信息
//	 */
//	@Override
//	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
//
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	/**
//	 * http安全配置
//	 * @param http http安全对象
//	 * @throws Exception http安全异常信息
//	 */
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		http.authorizeRequests()
//				.antMatchers("/swagger-ui.html").permitAll()
//				.antMatchers("/swagger-resources/**").permitAll()
//				.antMatchers("/images/**").permitAll()
//				.antMatchers("/webjars/**").permitAll()
//				.antMatchers("/v2/api-docs").permitAll()
//				.antMatchers("/configuration/ui").permitAll()
//				.antMatchers("/configuration/security").permitAll()
//				.antMatchers("/actuator/**").permitAll()
//				.antMatchers("/oauth/**").permitAll()
////                .antMatchers("/register", "/retrievePassword", "/registerVerificationCode", "/retrieveVerificationCode").permitAll()
////                .antMatchers("/customer/**").permitAll()
//				.anyRequest().authenticated()
//				.and()
//				.csrf().disable()
//				.formLogin().disable()
//				.sessionManagement().disable()
//				.cors()
//				.and()
//				.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
//				new Header("Access-Control-Expose-Headers", "Authorization"))))
//				.and()
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//		http.headers().cacheControl();
//	}

	//	 请配置这个，以保证在刷新Token时能成功刷新
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		// 配置用户来源于数据库
		// 配置密码加密方式  BCryptPasswordEncoder，添加用户加密的时候请也用这个加密
		auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Bean
	@Override
	protected UserDetailsService userDetailsService() {

		// 这里是添加两个用户到内存中去，实际中是从#下面去通过数据库判断用户是否存在
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		BCryptPasswordEncoder passwordEncode = new BCryptPasswordEncoder();
		String pwd = passwordEncode.encode("123456");
		manager.createUser(User.withUsername("user_1").password(pwd).authorities("USER").build());
		manager.createUser(User.withUsername("user_2").password(pwd).authorities("USER").build());
		return manager;

		// #####################实际开发中在下面写从数据库获取数据###############################
		// return new UserDetailsService() {
		// @Override
		// public UserDetails loadUserByUsername(String username) throws
		// UsernameNotFoundException {
		// // 通过用户名获取用户信息
		// boolean isUserExist = false;
		// if (isUserExist) {
		// //创建spring security安全用户和对应的权限（从数据库查找）
		// User user = new User("username", "password",
		// AuthorityUtils.createAuthorityList("admin", "manager"));
		// return user;
		// } else {
		// throw new UsernameNotFoundException("用户[" + username + "]不存在");
		// }
		// }
		// };

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.requestMatchers().anyRequest().and().authorizeRequests().antMatchers("/oauth/**").permitAll();
		// @formatter:on
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


}
