package com.cs.wework.mapper;

import com.cs.wework.entity.Chat;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Author: CSdw_wechat_group_operate_date
 * @Date: 2020/3/12 5:13 下午
 * @Description:
 */
@Repository
public interface ChatMapper extends Mapper<Chat>, MySqlMapper<Chat> {

    @Update("update enterprise_wechat_msg\n" +
            "set is_cancel = 1\n" +
            "    where is_cancel = 0\n" +
            "  and msg_id in (select temp.content from (select content from enterprise_wechat_msg where msg_type = 'revoke') temp\n" +
            ")")
    int checkIsCancel();
}
