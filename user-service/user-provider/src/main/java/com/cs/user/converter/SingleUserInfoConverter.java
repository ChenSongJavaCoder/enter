package com.cs.user.converter;

import com.cs.common.converter.SmartConverter;
import com.cs.user.converter.config.UserConverterConfig;
import com.cs.user.entity.User;
import com.cs.user.pojo.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author chenS
 * @Date 2019-11-05 21:52
 * @Description
 **/
@Component
public class SingleUserInfoConverter implements SmartConverter<User, UserInfo, UserConverterConfig> {

    @Override
    public UserInfo convert(User user) {
        return convert(user, new UserConverterConfig().defaultConfig());
    }

    @Override
    public UserInfo convert(User user, UserConverterConfig config) {
        if (Objects.isNull(user)) {
            return null;
        }

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo, config.ignoreProperties());

        return userInfo;
    }

}
