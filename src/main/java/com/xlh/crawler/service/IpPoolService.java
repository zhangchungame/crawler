package com.xlh.crawler.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xlh.crawler.dto.ProxyDaXiang;
import com.xlh.crawler.task.async.AsyncIpTest;
import com.xlh.crawler.task.async.AsyncTask;
import com.xlh.crawler.utils.HttpClientUtil;
import com.xlh.crawler.utils.ProxyUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IpPoolService {
    private static Logger logger = LoggerFactory.getLogger(IpPoolService.class);
    @Autowired
    private JedisPool jedisPool;



    public ProxyDaXiang getZhenxinIp(){
        Jedis jedis=jedisPool.getResource();
        String jsonstr=jedis.lpop("ListZhenxin");
        ProxyDaXiang proxyDaXiang=JSON.parseObject(jsonstr,ProxyDaXiang.class);
        if(jsonstr==null||jsonstr.equals("")){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jedis.close();
            return getZhenxinIp();
        }else{
            jedis.close();
            return proxyDaXiang;
        }
    }

    public ProxyDaXiang getZhuanliIp(){
        Jedis jedis=jedisPool.getResource();
        String jsonstr=jedis.lpop("ListZhuanli");
        ProxyDaXiang proxyDaXiang=JSON.parseObject(jsonstr,ProxyDaXiang.class);
        if(jsonstr==null||jsonstr.equals("")){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jedis.close();
            return getZhenxinIp();
        }else{
            jedis.close();
            return proxyDaXiang;
        }
    }




}
