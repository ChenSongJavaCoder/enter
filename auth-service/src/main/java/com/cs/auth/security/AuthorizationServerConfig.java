//package com.cs.auth.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @ClassName: AuthorizationServerConfig
// * @Author: CS
// * @Date: 2019/11/2 15:38
// * @Description: 授权服务配置
// */
//@Configuration
//@EnableAuthorizationServer
//public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//
//	//	// 认证管理器
////	@Autowired
////	private AuthenticationManager authenticationManager;
////	// redis连接工厂
////    /*@Autowired
////    private JedisConnectionFactory JedisConnectionFactory;*/
////	@Autowired
//	private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//	//
//	@Autowired
//	private DataSource dataSource;
//	//
////	@Bean
////	protected JwtAccessTokenConverter jwtAccessTokenConverter() {
////		JwtAccessTokenConverter converter = new MyJwtAccessTokenConverter();
////		converter.setKeyPair(RSAUtil.getKeyPair());
////		return converter;
////	}
////
////	@Bean
////	public TokenStore jwtTokenStore() {
////		return new JwtTokenStore(jwtAccessTokenConverter());
////	}
////
////	@Override
////	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
////		endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter()).authenticationManager(this.authenticationManager);
////	}
////
////	/**
////	 * OAuth 授权端点开放
////	 */
////	@Override
////	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
////		security
////				// 开启/oauth/token_key验证端口无权限访问
////				.tokenKeyAccess("permitAll()")
////				// 开启/oauth/check_token验证端口认证权限访问
////				.checkTokenAccess("isAuthenticated()")
////				//主要是让/oauth/token支持client_id以及client_secret作登录认证
////				.allowFormAuthenticationForClients();
////	}
////	/**
////	 * OAuth 配置客户端详情信息
////	 */
////
////	@Override
////	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
////		clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
////	}
//// 资源ID
//	private static final String SOURCE_ID = "order";
//	private static final int ACCESS_TOKEN_TIMER = 60 * 60 * 24;
//	private static final int REFRESH_TOKEN_TIMER = 60 * 60 * 24 * 30;
//
//	@Autowired
//	AuthenticationManager authenticationManager;
//
//	@Autowired
//	RedisConnectionFactory redisConnectionFactory;
//
//	@Override
//	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
////		clients.inMemory().withClient("myapp")
////				.resourceIds(SOURCE_ID)
////				.authorizedGrantTypes("password", "refresh_token")
////				.scopes("all")
////				.authorities("ADMIN")
////				.secret(passwordEncoder.encode("lxapp"))
////				.accessTokenValiditySeconds(ACCESS_TOKEN_TIMER)
////				.refreshTokenValiditySeconds(REFRESH_TOKEN_TIMER);
//
//		clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
//	}
//
//	@Override
//	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		endpoints.accessTokenConverter(accessTokenConverter())
//				.tokenStore(tokenStore())
//				.authenticationManager(authenticationManager);
//	}
//
//	@Override
//	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//		// 允许表单认证
//		oauthServer.allowFormAuthenticationForClients();
//	}
//
//	// JWT
//	@Bean
//	public JwtAccessTokenConverter accessTokenConverter() {
//		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter() {
//			/***
//			 * 重写增强token方法,用于自定义一些token总需要封装的信息
//			 */
//			@Override
//			public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//				String userName = authentication.getUserAuthentication().getName();
//				// 得到用户名，去处理数据库可以拿到当前用户的信息和角色信息（需要传递到服务中用到的信息）
//				final Map<String, Object> additionalInformation = new HashMap<>();
//				// Map假装用户实体
//				Map<String, String> userinfo = new HashMap<>();
//				userinfo.put("id", "1");
//				userinfo.put("username", "LiaoXiang");
//				userinfo.put("qqnum", "438944209");
//				userinfo.put("userFlag", "1");
//				additionalInformation.put("userinfo", userinfo.toString());
//				((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
//				OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
//				return enhancedToken;
//			}
//		};
//		// 测试用,资源服务使用相同的字符达到一个对称加密的效果,生产时候使用RSA非对称加密方式
//		accessTokenConverter.setSigningKey("SigningKey");
//		return accessTokenConverter;
//	}
//
//	@Bean
//	public TokenStore tokenStore() {
//		RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
//		return tokenStore;
//	}
//
//}
