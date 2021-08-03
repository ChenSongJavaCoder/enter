package com.cs.demo.chain.leave;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: CS
 * @date: 2021/8/3 下午2:14
 * @description:
 */
@Data
@ToString
public class LeaveResponse {

    private List<Node> nodes = new ArrayList<>();

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Node {
        private String managerName;
        private boolean success;
        private String message;
    }
}


