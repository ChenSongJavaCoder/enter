//package com.cs.auth.security;
//
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//
//import java.util.HashMap;
//
///**
// * @ClassName: MyJwtAccessTokenConverter
// * @Author: CS
// * @Date: 2019/11/2 16:21
// * @Description:
// */
//public class MyJwtAccessTokenConverter extends JwtAccessTokenConverter {
//
//	@Override
//	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//		if (accessToken instanceof DefaultOAuth2AccessToken) {
//			// ((DefaultOAuth2AccessToken) accessToken).setRefreshToken();
//			Object principal = authentication.getPrincipal();
//			if (principal instanceof OauthUser) {
//				OauthUser user = (OauthUser) principal;
//				HashMap<String, Object> map = new HashMap<>();
//				map.put("userId", user.getBaseOperator().getId());
//				map.put("name", user.getBaseOperator().getName());
//				((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
//			}
//		}
//		return super.enhance(accessToken, authentication);
//	}
//
//}
