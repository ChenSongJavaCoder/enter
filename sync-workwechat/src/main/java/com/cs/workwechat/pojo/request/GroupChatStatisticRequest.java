package com.cs.workwechat.pojo.request;

import lombok.Data;

import java.util.List;

/**
 * @Author: CS
 * @Date: 2020/6/2 4:59 下午
 * @Description:
 */
@Data
public class GroupChatStatisticRequest {


    /**
     * day_begin_time : 1572505490
     * owner_filter : {"userid_list":["zhangsan"],"partyid_list":[7]}
     * order_by : 2
     * order_asc : 0
     * offset : 0
     * limit : 1000
     */

    private Long day_begin_time;
    private OwnerFilterBean owner_filter;
    /**
     * 排序方式。
     * 1 - 新增群的数量
     * 2 - 群总数
     * 3 - 新增群人数
     * 4 - 群总人数
     * <p>
     * 默认为1
     */
    private Integer order_by;
    private Integer order_asc;
    private Integer offset;
    private Integer limit;


    @Data
    public static class OwnerFilterBean {
        private List<String> userid_list;
        private List<Integer> partyid_list;
    }
}
