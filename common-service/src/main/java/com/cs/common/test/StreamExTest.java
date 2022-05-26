package com.cs.common.test;

import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.Map;

/**
 * @author: CS
 * @date: 2022/5/23 下午2:12
 * @description:
 */
public class StreamExTest {

    static List<String> demoList = Lists.newArrayList("1", "2", "3");
    static List<User> users = Lists.newArrayList(User.of(1, "bob"), User.of(2, "lily"), User.of(2, "tom"));

    public static void main(String[] args) {
        String joining = StreamEx.of(demoList).joining(",");
        System.out.println("joining:" + joining);
        List<String> prepend = StreamEx.of(demoList).prepend("0").append("4").toList();
        System.out.println("prepend:" + prepend);
        List<String> append = StreamEx.of(demoList).append("4").toList();
        System.out.println("append:" + append);
        Map<Integer, List<User>> ageMap = StreamEx.of(users).groupingBy(User::getAge);
        System.out.println("asMap:" + ageMap.toString());
    }


    public static class User {
        Integer age;
        String name;

        public User(Integer age, String name) {
            this.age = age;
            this.name = name;
        }

        public static User of(Integer age, String name) {
            return new User(age, name);
        }

        public Integer getAge() {
            return age;
        }

        public String getName() {
            return name;
        }
    }
}
