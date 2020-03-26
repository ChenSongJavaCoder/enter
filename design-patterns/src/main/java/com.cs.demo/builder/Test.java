package com.cs.demo.builder;

/**
 * @Author: CS
 * @Date: 2020/3/26 2:40 下午
 * @Description:
 */
public class Test {

    public static void main(String[] args) {
        User user = new
                User.UserBuilder("chensong", 26, 1)
                .address("hangzhou").tel("13349257109").build();

    }
}
