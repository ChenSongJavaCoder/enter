package com.cs.auth.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cs.auth.feign.FeignUserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author keson
 * @date 2018/12/16 21:49
 */
public class JwtUserService implements UserDetailsService {

	/**
	 * 端登录过期时间
	 */
	private static Long TOKEN_EXPIRED_SECONDS = 60L * 60L;

	private PasswordEncoder passwordEncoder;

	@Autowired
	FeignUserApi feignUserApi;

	@Autowired
	UsernamePasswordUserService usernamePasswordUserService;

	@Autowired
	public JwtUserService() {
		this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	public UserDetails getUserLoginInfoByMobile(String username) {
		String salt = "123456ef";
		UserDetails user = loadUserByUsername(username);
		//将salt放到password字段返回
		return User.builder().username(user.getUsername()).password(salt).authorities(user.getAuthorities()).build();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usernamePasswordUserService.loadUserByUsername(username);
	}

	public void deleteUserLoginInfo(String username) {
		/**
		 * @todo 清除数据库或者缓存中登录salt
		 */
	}


	/**
	 * 创建PC登录使用的JWT token
	 *
	 * @param userDetails
	 * @return
	 */
	public String createPcAuthorizationToken(UserDetails userDetails) {
		//BCrypt.gensalt();  正式开发时可以调用该方法实时生成加密的salt
		String salt = "123456ef";
		Algorithm algorithm = Algorithm.HMAC256(salt);
		LocalDateTime dateTime = LocalDateTime.now();
		return JWT.create()
				.withSubject(userDetails.getUsername())
				.withIssuedAt(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()))
				.withExpiresAt(Date.from(dateTime.plusSeconds(TOKEN_EXPIRED_SECONDS).atZone(ZoneId.systemDefault()).toInstant()))
				.sign(algorithm);
	}

	/**
	 * JWT token 刷新机制实现：按照JWT token中原有的失效期限顺延同等时间 NOTE:后续也可扩展为使用策略实现，目前需求没必要如此处理
	 *
	 * @param jwt
	 * @param authentication
	 * @return
	 */
	public String jwtAuthorizationTokenRefresh(DecodedJWT jwt, Authentication authentication) {
		String salt = "123456ef";

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256(salt);
		LocalDateTime dateTime = LocalDateTime.now();

		Duration duration = Duration.between(
				LocalDateTime.ofInstant(jwt.getIssuedAt().toInstant(), ZoneId.systemDefault()),
				LocalDateTime.ofInstant(jwt.getExpiresAt().toInstant(), ZoneId.systemDefault())
		);

		return JWT.create()
				.withSubject(userDetails.getUsername())
				.withIssuedAt(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()))
				.withExpiresAt(Date.from(dateTime.plus(duration).atZone(ZoneId.systemDefault()).toInstant()))
				.sign(algorithm);
	}
}
