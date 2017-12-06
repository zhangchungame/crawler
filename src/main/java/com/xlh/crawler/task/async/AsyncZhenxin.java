package com.xlh.crawler.task.async;

import com.xlh.crawler.dto.CdmEntDtoCorpInfo;
import com.xlh.crawler.dto.ProxyDaXiang;
import com.xlh.crawler.dto.SqlLimit;
import com.xlh.crawler.mapper.CdmEntDtoCorpInfoMapper;
import com.xlh.crawler.mapper.CraCorpInfoMapper;
import com.xlh.crawler.service.IpPoolService;
import com.xlh.crawler.utils.HttpClientUtil;
import com.xlh.crawler.utils.ProxyUtil;
import com.xlh.crawler.utils.ShZhenXinThread;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

@Component
public class AsyncZhenxin {
    private static Logger logger = LoggerFactory.getLogger(AsyncZhenxin.class);

    @Autowired
    private IpPoolService ipPoolService;

    @Autowired
    private CdmEntDtoCorpInfoMapper cdmEntDtoCorpInfoMapper;
    @Autowired
    private CraCorpInfoMapper craCorpInfoMapper;
    @Async("taskExecutorZhenxin")
    public void startUp(){
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Example example = new Example(CdmEntDtoCorpInfo.class);
            example.createCriteria().andLike("enterpriseName", "%上海%").andEqualTo("status", 0);
            int count = cdmEntDtoCorpInfoMapper.selectCountByExample(example);

            SqlLimit sqlLimit = new SqlLimit();
            int page = 0;
            int totalPage = count / 10;


            Semaphore semaphore=new Semaphore(10);//总共有5个许可
            while (page <= totalPage) {
                sqlLimit.setStart(0);
                sqlLimit.setLimit(10);
                List<CdmEntDtoCorpInfo> list = cdmEntDtoCorpInfoMapper.selectByPage(sqlLimit);
                if(list.size()<1){
                    break;
                }
                ProxyDaXiang daXiang= ipPoolService.getZhenxinIp();
                for (int i = 0; i < list.size(); i++) {
                    try {
                        semaphore.acquire();
                        ShZhenXinThread shZhenXinThread= new ShZhenXinThread(semaphore,list.get(i),daXiang);
                        shZhenXinThread.setCdmEntDtoCorpInfoMapper(cdmEntDtoCorpInfoMapper);
                        shZhenXinThread.setCraCorpInfoMapper(craCorpInfoMapper);
                        shZhenXinThread.setIpPoolService(ipPoolService);
                        shZhenXinThread.start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                page++;
            }
        }
    }

}
