package com.xlh.crawler.controller;

import com.alibaba.fastjson.JSON;
import com.xlh.crawler.dto.CdmEntDtoCorpInfo;
import com.xlh.crawler.dto.SqlLimit;
import com.xlh.crawler.mapper.CdmEntDtoCorpInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private CdmEntDtoCorpInfoMapper cdmEntDtoCorpInfoMapper;
    @RequestMapping("/test")
    public String test(){
        SqlLimit sqlLimit=new SqlLimit();
        sqlLimit.setStart(0);
        sqlLimit.setLimit(10);
        List<CdmEntDtoCorpInfo> list=cdmEntDtoCorpInfoMapper.selectByPage(sqlLimit);

        System.out.println(JSON.toJSONString(list));
        return "test";
    }
}
