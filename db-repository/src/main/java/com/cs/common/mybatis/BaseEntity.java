package com.cs.common.mybatis;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.LogicDelete;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @ClassName: BaseEntity
 * @Author: CS
 * @Date: 2019/11/2 10:21
 * @Description: 基础实体属性
 */
@Data
@ApiModel
@Accessors(chain = true)
@EqualsAndHashCode
public class BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 最后修改时间
     */
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

    /**
     * 修改人
     */
    @Column(name = "modify_by")
    private String modifyBy;

    /**
     * 逻辑删除标识 TODO @LogicDeleted
     */
    @LogicDelete
    @Column(name = "deleted")
    private Boolean deleted;

}
