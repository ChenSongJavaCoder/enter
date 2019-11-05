package com.cs.user.converter.config;

import com.cs.common.converter.AbstractConverterConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author chenS
 * @Date 2019-11-05 21:59
 * @Description
 **/
@Data
@ApiModel
@Accessors(chain = true)
public class UserConverterConfig extends AbstractConverterConfig<UserConverterConfig> {


    private boolean aConverter;

    @Override
    public UserConverterConfig defaultConfig() {
        this.aConverter = true;
        return this;
    }
}
