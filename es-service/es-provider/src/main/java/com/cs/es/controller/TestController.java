package com.cs.es.controller;

import com.cs.common.bean.PagedResult;
import com.cs.common.bean.Result;
import com.cs.es.common.EsIndices;
import com.cs.es.common.EsRestService;
import com.cs.es.document.EsUserInfo;
import com.cs.es.entity.BmSpfl;
import com.cs.es.entity.KeywordMapping;
import com.cs.es.entity.KeywordMatch;
import com.cs.es.mapper.KeywordMappingMapper;
import com.cs.es.mapper.KeywordMatchMapper;
import com.cs.es.model.DemoTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author chenS
 * @Date 2019-12-04 14:57
 * @Description
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    EsRestService esRestService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KeywordMappingMapper keywordMappingMapper;

    @Autowired
    KeywordMatchMapper keywordMatchMapper;


    @ApiOperation("测试连接")
    @GetMapping("client")
    public String testClient() throws JsonProcessingException {
        DemoTest demoTest = new DemoTest();
        demoTest.setName("cs");
        demoTest.setAge(22);
        demoTest.setSex(1);
        esRestService.createOrUpdateDocument(EsIndices.DEMO, null, objectMapper.writeValueAsString(demoTest));
        return esRestService.testClient();
    }

    @ApiOperation("新增数据")
    @GetMapping("add")
    public String add() throws JsonProcessingException {
        BmSpfl bmSpfl = new BmSpfl();
        bmSpfl.setBm("4567898765678");
        bmSpfl.setHbbm("fghjhghjkhjk");
        bmSpfl.setMc("融易算专有分类");

        esRestService.createOrUpdateDocument(EsIndices.BM_SPFL, null, objectMapper.writeValueAsString(bmSpfl));
        return esRestService.testClient();
    }

    @ApiOperation("query")
    @GetMapping("query/{keyword}")
    public PagedResult<BmSpfl> query(@PathVariable String keyword) throws JsonProcessingException {
        PagedResult<BmSpfl> list = esRestService.pagedSearch(EsIndices.BM_SPFL, keyword, null, new TypeReference<BmSpfl>() {
        }, 0, 10, null, true);

        return list;
    }

    @ApiOperation("新增关键字映射")
    @PostMapping("addKeywordMapping")
    public Result<String> addKeywordMapping(@RequestBody KeywordMapping keywordMapping) throws JsonProcessingException {
        keywordMappingMapper.insertSelective(keywordMapping);
        return Result.success().build();
    }

    @ApiOperation("新增关键字匹配")
    @PostMapping("addKeywordMatch")
    public Result<String> addKeywordMatch(@RequestBody KeywordMatch keywordMatch) throws JsonProcessingException {
        keywordMatchMapper.insertSelective(keywordMatch);
        return Result.success().build();
    }

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @ApiOperation("查询EsUserInfo")
    @GetMapping("queryEsUserInfo")
    public Result<List<EsUserInfo>> queryEsUserInfo() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withPageable(PageRequest.of(0, 100))
                .withIndices("es_user_info")
                .build();
        List<EsUserInfo> list = elasticsearchRestTemplate.queryForList(searchQuery, EsUserInfo.class);
        return Result.success().data(list).build();
    }

    @ApiOperation("更新字段")
    @GetMapping("update")
    public Result<String> update(){
        return Result.success().data(esRestService.updateDocument()).build();
    }
}
