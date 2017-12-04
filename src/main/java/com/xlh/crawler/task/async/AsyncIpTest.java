package com.xlh.crawler.task.async;

import com.xlh.crawler.dto.ProxyDaXiang;
import com.xlh.crawler.service.IpPoolService;
import com.xlh.crawler.utils.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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

import java.io.IOException;
import java.util.concurrent.Semaphore;

@Component
public class AsyncIpTest{
    private static Logger logger = LoggerFactory.getLogger(AsyncIpTest.class);
    @Autowired
    private JedisPool jedisPool;

    @Async("taskExecutorOne")
    public void testIp(ProxyDaXiang daXiang, Semaphore semaphore) throws InterruptedException {
        logger.info("小于要求数量，开始拉取 =={}",daXiang.toString());
        Jedis jedis = jedisPool.getResource();
        if(testZhuanli(daXiang)){
            jedis.rpush("ListZhuanli",daXiang.toString());
        }
        if(testZhenxin(daXiang)){
            jedis.rpush("ListZhenxin",daXiang.toString());
        }
        jedis.close();
        semaphore.release();
    }

    private boolean testZhuanli(ProxyDaXiang daXiang){
        try{
            logger.info("testZhuanli =="+daXiang.toString());
            CloseableHttpClient client = HttpClientUtil.generateClient(daXiang);
            HttpGet httpGet=new HttpGet("http://cpquery.sipo.gov.cn/txnPantentInfoList.do");
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String resp= EntityUtils.toString(entity,"UTF-8");
            if(resp.equals("")){
                throw new Exception("resp is empty");
            }
            return  true;
        }catch (Exception e){

            logger.info("testZhuanli exception =={}",e.getMessage());
        }
        return false;
    }



    private boolean testZhenxin(ProxyDaXiang daXiang){
        try{
            logger.info("testZhenxin >>>"+daXiang.toString());
            CloseableHttpClient client = HttpClientUtil.generateClient(daXiang);
            HttpGet httpGet=new HttpGet("http://www.baidu.com/");
            HttpResponse response = client.execute(httpGet);
            return  true;
        }catch (ClientProtocolException e) {
            logger.info("ClientProtocolException ={}",e.getMessage());
        } catch (IOException e) {
            logger.info("IOException ={}",e.getMessage());
        }
        return  false;
    }

}
