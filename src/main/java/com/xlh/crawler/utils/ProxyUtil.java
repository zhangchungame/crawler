package com.xlh.crawler.utils;

import com.xlh.crawler.dto.ProxyDaXiang;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProxyUtil {
    private static Logger logger= LoggerFactory.getLogger(ProxyUtil.class);
    private static List<ProxyDaXiang> daXiangList=new ArrayList<ProxyDaXiang>();
    private static int i=0;

    public static ProxyDaXiang getProxy() throws IOException {
        if(i>=daXiangList.size()){
            daXiangList=new ArrayList<ProxyDaXiang>();
            i=0;
            CloseableHttpClient client = HttpClientUtil.generateClient(null);
            HttpGet httpGet=new HttpGet("http://tvp.daxiangdaili.com/ip/?tid=557552170840411&num=3&delay=1&filter=on&area=上海");
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            if(result.contains("ERROR")){
                httpGet=new HttpGet("http://tvp.daxiangdaili.com/ip/?tid=557552170840411&num=3&delay=3&filter=on");
                response = client.execute(httpGet);
                entity = response.getEntity();
                result = EntityUtils.toString(entity, "UTF-8");
                if(result.contains("ERROR")){
                    httpGet=new HttpGet("http://tvp.daxiangdaili.com/ip/?tid=557552170840411&num=3");
                    response = client.execute(httpGet);
                    entity = response.getEntity();
                    result = EntityUtils.toString(entity, "UTF-8");
                }
            }
            logger.info("proxy result={}",result);
            String[] ips=result.split("\r\n");
            for(int j=0;j<ips.length;j++){
                String[] ss=ips[j].split(":");
                ProxyDaXiang proxyDaXiang=new ProxyDaXiang();
                proxyDaXiang.setIp(ss[0]);
                proxyDaXiang.setPort(ss[1]);
                daXiangList.add(proxyDaXiang);
            }
        }
        ProxyDaXiang res=daXiangList.get(i);
        i++;
        return res;
    }
}
