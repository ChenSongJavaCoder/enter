//package com.cs.auth.security;
//
//import com.cs.user.pojo.UserDto;
//import org.springframework.security.core.CredentialsContainer;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
///**
// * @ClassName: OauthUser
// * @Author: CS
// * @Date: 2019/11/2 16:16
// * @Description:
// */
//public class OauthUser implements UserDetails, CredentialsContainer {
//
//	private final UserDto baseUser;
//	private final User user;
//
//	public OauthUser(UserDto baseUser, User user) {
//		this.baseUser = baseUser;
//		this.user = user;
//	}
//
//	@Override
//	public void eraseCredentials() {
//		user.eraseCredentials();
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return user.getAuthorities();
//	}
//
//	@Override
//	public String getPassword() {
//		return user.getPassword();
//	}
//
//	@Override
//	public String getUsername() {
//		return user.getUsername();
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		return user.isAccountNonExpired();
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return user.isAccountNonLocked();
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return user.isCredentialsNonExpired();
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return user.isEnabled();
//	}
//
//	public UserDto getBaseOperator() {
//		return baseUser;
//	}
//
//}
