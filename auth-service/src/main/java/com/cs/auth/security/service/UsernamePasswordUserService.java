package com.cs.auth.security.service;

import com.cs.auth.feign.FeignUserApi;
import com.cs.common.bean.Result;
import com.cs.user.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


/**
 * @ClassName: UserServiceImpl
 * @Author: CS
 * @Date: 2019/11/2 11:02
 * @Description:
 */
@Component
public class UsernamePasswordUserService implements UserDetailsService {

    @Autowired
    private FeignUserApi feignUserApi;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        Result<UserInfo> userInfoResult = feignUserApi.getUserInfoByUsername(username);
        //需要构造org.springframework.security.core.userdetails.User 对象包含账号密码还有用户的角色
        if (userInfoResult.isSuccess()) {
            UserInfo user = userInfoResult.getData();
            User u = new User(String.valueOf(user.getId()), user.getPassword(), AuthorityUtils.createAuthorityList("USER"));
            return u;
        } else {
            throw new UsernameNotFoundException("用户[" + username + "]不存在");
        }
    }
}