package com.cs.user.converter;

import com.cs.common.converter.SmartConverter;
import com.cs.user.converter.config.UserConverterConfig;
import com.cs.user.entity.User;
import com.cs.user.pojo.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author chenS
 * @Date 2019-11-06 15:13
 * @Description
 **/
@Component
public class ListUserInfoConverter implements SmartConverter<List<User>, List<UserInfo>, UserConverterConfig> {

    @Override
    public List<UserInfo> convert(List<User> users, UserConverterConfig config) {
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }

        //TODO use config
        List<UserInfo> userInfos = users.stream().map(p -> {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(p, userInfo, "password");
            return userInfo;
        }).collect(Collectors.toList());

        return userInfos;
    }

    @Override
    public List<UserInfo> convert(List<User> users) {
        return convert(users, new UserConverterConfig().defaultConfig());
    }
}
