package com.cs.es.mapper;

import com.cs.common.mybatis.BaseMapper;
import com.cs.es.entity.KeywordMatch;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: CS
 * @date: 2021/5/6 下午6:45
 * @description:
 */
public interface KeywordMatchMapper extends BaseMapper<KeywordMatch> {

    /**
     * 根据关键字统计倒序排列
     *
     * @param keyword
     * @return
     */
    @Select("SELECT bm,count(*) cnt from keyword_match WHERE keyword = #{keyword} GROUP BY bm ORDER BY cnt desc")
    List<String> selectBmByKeywordAndOrderBy(@Param("keyword") String keyword);
}
