package com.xlh.crawler.utils;

import com.xlh.crawler.dto.ProxyDaXiang;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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

    public synchronized static ProxyDaXiang getProxy() throws IOException {
        if(i>=daXiangList.size()){
            logger.info("重新拿");
            daXiangList=new ArrayList<ProxyDaXiang>();
            i=0;
            CloseableHttpClient client = HttpClientUtil.generateClient(null);
            HttpGet httpGet=new HttpGet("http://pvt.daxiangdaili.com/ip/?tid=557552170840411&num=2&category=2&sortby=time");
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            if(result.contains("ERROR")){
                httpGet=new HttpGet("http://tvp.daxiangdaili.com/ip/?tid=557552170840411&num=5&delay=3&filter=on");
                response = client.execute(httpGet);
                entity = response.getEntity();
                result = EntityUtils.toString(entity, "UTF-8");
                if(result.contains("ERROR")){
                    httpGet=new HttpGet("http://tvp.daxiangdaili.com/ip/?tid=557552170840411&num=5");
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
                if(testIp(proxyDaXiang)){
                    daXiangList.add(proxyDaXiang);
                }
            }
        }
        logger.info("老的数据");
        if(daXiangList.size()<=i){
            return getProxy();
        }
        ProxyDaXiang res=daXiangList.get(i);
        i++;
        return res;
    }
    private static boolean testIp(ProxyDaXiang proxyDaXiang){
        try{
            CloseableHttpClient client = HttpClientUtil.generateClient(proxyDaXiang);
//            HttpGet httpGet=new HttpGet("http://www.baidu.com/");
            HttpGet httpGet=new HttpGet("http://ln.gsxt.gov.cn/saicpub/");
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String resp= EntityUtils.toString(entity,"UTF-8");
            if(resp.contains("500 Internal Privoxy Error")){
                return false;
            }
            return  true;
        }catch (ClientProtocolException e) {
            logger.info("ClientProtocolException ={}",e.getMessage());
        } catch (IOException e) {
            logger.info("IOException ={}",e.getMessage());
        }
        return  false;
    }
}
