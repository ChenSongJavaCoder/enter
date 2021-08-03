package com.cs.demo.chain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: CS
 * @date: 2021/8/3 上午11:05
 * @description:
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String name;

    private Integer age;
}
