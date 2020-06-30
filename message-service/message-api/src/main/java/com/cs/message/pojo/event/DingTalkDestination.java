package com.cs.message.pojo.event;

import lombok.AllArgsConstructor;

/**
 * @Author: CS
 * @Date: 2020/6/12 11:03 上午
 * @Description: 枚举类型不易维护, 可以考虑直接传参token或者用数据库维护
 */
@AllArgsConstructor
public enum DingTalkDestination {

    DEFAULT_TEST("51e7c97226902b2760f016df93c2b6b0b957a157b63a29410bcf439aa170398f", true);
    /**
     * access_token
     */
    private String token;
    /**
     * 功能是否开启
     */
    private Boolean open;


    public String getTokenByAccess() {
        return open ? token : null;
    }

}
