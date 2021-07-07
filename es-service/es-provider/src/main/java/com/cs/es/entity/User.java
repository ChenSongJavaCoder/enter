package com.cs.es.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author: CS
 * @date: 2021/6/10 下午3:22
 * @description:
 */
@Data
@Accessors(chain = true)
@Table
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String nickname;
    private Integer age;
    private Integer sex;
    private String password;
    private LocalDateTime createTime;
}
