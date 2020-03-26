package com.cs.demo.builder;

import lombok.Getter;

/**
 * @Author: CS
 * @Date: 2020/3/26 2:17 下午
 * @Description:
 */
@Getter
public class User {
    /**
     * require parameters
     */
    private final String name;
    private final Integer age;
    private final Integer gender;

    /**
     * optional parameters
     */
    private final String address;
    private final String tel;

    public static class UserBuilder implements Builder<User> {
        private String name;
        private Integer age;
        private Integer gender;

        private String address;
        private String tel;

        // 必填参数，需要通过构造器进行属性赋值
        public UserBuilder(String name, Integer age, Integer gender) {
            this.name = name;
            this.age = age;
            this.gender = gender;
        }


        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }

        public UserBuilder tel(String tel) {
            this.tel = tel;
            return this;
        }

        @Override
        public User build() {
            return new User(this);
        }
    }

    private User(UserBuilder builder) {
        name = builder.name;
        age = builder.age;
        gender = builder.gender;
        address = builder.address;
        tel = builder.tel;
    }

}
