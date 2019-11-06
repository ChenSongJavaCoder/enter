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


    private boolean showPassword;
    private boolean showDeleted;

    @Override
    public UserConverterConfig defaultConfig() {
        this.showPassword = false;
        this.showDeleted = false;
        return this;
    }

    @Override
    public String[] ignoreProperties() {
        String[] ignoreProperties = new String[2];
        if (!this.showDeleted) {
            ignoreProperties[0] = "deleted";
        }
        if (!this.showPassword) {
            ignoreProperties[1] = "password";
        }
        return ignoreProperties;
    }
}
