package com.cs.user.entity;

import com.cs.common.mybatis.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Table;

/**
 * @ClassName: User
 * @Author: CS
 * @Date: 2019/11/2 10:48
 * @Description:
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "user_cs")
public class User extends BaseEntity {

    /**
     * 用户名 可用作登陆，具有唯一性（eg：工号，手机号）
     */
    private String username;

    /**
     * 姓名 昵称 用作日常展示 eg：霸王龙
     */
    private String nickname;

    private Integer age;

    private String password;
}
