package com.cs.es.entity;

import com.cs.es.binlog.annotation.TableMapping;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author: CS
 * @date: 2021/5/6 下午4:54
 * @description:
 */
@Data
@Accessors(chain = true)
@Table(name = "bm_spfl")
@TableMapping(databaseName = "mine")
public class BmSpfl {
    /**
     * 编码
     */
    private String bm;

    /**
     * 合并编码
     */
    private String hbbm;

    /**
     * 名称
     */
    private String mc;

    /**
     * 说明
     */
    private String sm;

    /**
     * 税率
     */
    private String slv;

    /**
     * 增值税特殊管理
     */
    private String zzstsgl;

    /**
     * 增值税组成意见
     */
    private String zzszcyj;

    /**
     * 增值税特殊内容代码
     */
    private String zzstsnrdm;

    /**
     * 消费税管理
     */
    private String xfsgl;

    /**
     * 消费税组成意见
     */
    private String xfszcyj;

    /**
     * 消费税特殊内容代码
     */
    private String xfstsnrdm;

    /**
     * 关键字
     */
    private String gjz;

    /**
     * 汇总项
     */
    private String hzx;

    /**
     * 统计局编码
     */
    private String tjjbm;

    /**
     * 海关进出口商品品目
     */
    private String hgjcksppm;

    /**
     * 编码表版本号
     */
    private String bmbBbh;

    /**
     * 版本号
     */
    private String bbh;

    /**
     * kyzt
     */
    private String kyzt;

    /**
     * 启用时间
     */
    private String qysj;

    /**
     * 过度期截止时间
     */
    private String gdqjzsj;

    /**
     * 上级编码
     */
    private String sjbm;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime gxsj;

    /**
     * wj
     */
    private Integer wj;

    /**
     * ishide
     */
    private Integer ishide;

    /**
     * slvselect
     */
    private Float slvselect;

    /**
     * 优惠政策
     */
    private String yhzc;

    /**
     * 优惠政策名称
     */
    private String yhzcmc;

    /**
     * lslvbs
     */
    private String lslvbs;

    /**
     * 分类编码简称
     */
    private String flbmjc;

    /**
     * 使用汇率
     */
    private String syhlv;

    /**
     * 创建时间
     */
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间自动维护
     */
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 删除标志 0：未删除 1：已删除
     */
    @JsonIgnore
    private Integer delFlag;
}
