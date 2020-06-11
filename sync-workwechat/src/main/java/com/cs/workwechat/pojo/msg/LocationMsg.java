package com.cs.workwechat.pojo.msg;

import com.cs.workwechat.entity.BaseMsg;
import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class LocationMsg extends BaseMsg<LocationMsg.Location> {

    private Location location;

    @Data
    public static class Location {

        /**
         * 地址信息。String类型
         */
        private String address;

        /**
         * 位置信息的title。String类型
         */
        private String title;

        /**
         * 经度，单位double
         */
        private Double longitude;

        /**
         * 纬度，单位double
         */
        private Double latitude;

        /**
         * 缩放比例。Uint32类型
         */
        private Integer zoom;
    }
}
