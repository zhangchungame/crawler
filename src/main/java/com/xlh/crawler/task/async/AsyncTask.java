package com.xlh.crawler.task.async;

import com.xlh.crawler.dto.CraCorpInfo;
import com.xlh.crawler.dto.ProxyDaXiang;
import com.xlh.crawler.dto.SqlLimit;
import com.xlh.crawler.mapper.CraCorpInfoMapper;
import com.xlh.crawler.service.IpPoolService;
import com.xlh.crawler.utils.HttpClientUtil;
import com.xlh.crawler.utils.ShZhenXinThread;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.RowBounds;
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
public class AsyncTask {
    private static Logger logger = LoggerFactory.getLogger(AsyncTask.class);

    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private AsyncIpTest asyncIpTest;

    @Autowired
    private AsyncZhuanli asyncZhuanli;

    @Autowired
    private IpPoolService ipPoolService;
    @Autowired
    private CraCorpInfoMapper craCorpInfoMapper;

    @Async("taskExecutorOne")
    public void ipPoolInsert() {
        Jedis jedis = jedisPool.getResource();
        Semaphore semaphore = new Semaphore(8);//总共有5个许可
        while (true) {
            if (jedis.llen("ListZhenxin") < 4 || jedis.llen("ListZhuanli") < 4) {
//            if(jedis.llen("ListZhenxin")<4){
                logger.info("小于要求数量，开始拉取");
                try {
                    List<ProxyDaXiang> listProxy = getIp();
                    for (ProxyDaXiang daXiang : listProxy) {
                        semaphore.acquire();
                        asyncIpTest.testIp(daXiang, semaphore);
                    }
                } catch (Exception e) {
                    logger.info("exception={}",e.getMessage());
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Async("taskExecutorZhuanli")
    public void startUp() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Example example = new Example(CraCorpInfo.class);
            example.createCriteria().andEqualTo("rightStatus", 0);
            int count = craCorpInfoMapper.selectCountByExample(example);

            SqlLimit sqlLimit = new SqlLimit();
            int page = 0;
            int totalPage = count / 10;


            Semaphore semaphore = new Semaphore(10);//总共有5个许可

            while (page <= totalPage) {
                try {
                    ProxyDaXiang daXiang = ipPoolService.getZhuanliIp();
                    RowBounds rowBounds = new RowBounds(0, 15);
                    List<CraCorpInfo> list = craCorpInfoMapper.selectByExampleAndRowBounds(example, rowBounds);
                    if (list.size() < 1) {
                        break;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        semaphore.acquire();
                        asyncZhuanli.dealResp(list.get(i), daXiang, semaphore);
                    }
                    page++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<ProxyDaXiang> getIp() throws Exception {
//        ProxyDaXiang proxyDaXiang=new ProxyDaXiang();
//        List<ProxyDaXiang> list=new ArrayList<>();
//        for(int i=0;i<10;i++){
//            proxyDaXiang.setPort("11");
//            proxyDaXiang.setIp(String.valueOf(i));
//            list.add(proxyDaXiang);
//        }
//        return list;
        CloseableHttpClient client = HttpClientUtil.generateClient(null);
        HttpGet httpGet = new HttpGet("http://tvp.daxiangdaili.com/ip/?tid=557552170840411&num=20&filter=on");
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        if (result.contains("ERROR")) {
            throw new Exception("拉取IP接口失败"+result);
        }
        logger.info("proxy result={}", result);
        List<ProxyDaXiang> daXiangList = new ArrayList<>();
        String[] ips = result.split("\r\n");
        for (int j = 0; j < ips.length; j++) {
            String[] ss = ips[j].split(":");
            ProxyDaXiang proxyDaXiang = new ProxyDaXiang();
            proxyDaXiang.setIp(ss[0]);
            proxyDaXiang.setPort(ss[1]);
            daXiangList.add(proxyDaXiang);
        }
        return daXiangList;
    }

}
