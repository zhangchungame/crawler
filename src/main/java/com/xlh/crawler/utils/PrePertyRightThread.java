package com.xlh.crawler.utils;

import com.alibaba.fastjson.JSON;
import com.xlh.crawler.dto.CraCorpInfo;
import com.xlh.crawler.dto.PrepertyRightInfo;
import com.xlh.crawler.dto.ProxyDaXiang;
import com.xlh.crawler.mapper.PrepertyRightInfoMapper;
import net.sourceforge.tess4j.TesseractException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrePertyRightThread  {


    private PrepertyRightInfoMapper prepertyRightInfoMapper;

    public PrepertyRightInfoMapper getPrepertyRightInfoMapper() {
        return prepertyRightInfoMapper;
    }

    public void setPrepertyRightInfoMapper(PrepertyRightInfoMapper prepertyRightInfoMapper) {
        this.prepertyRightInfoMapper = prepertyRightInfoMapper;
    }

    public String getRespons(HttpClient client, String enterpriseName) throws Exception {
        HttpGet httpGet=new HttpGet("http://cpquery.sipo.gov.cn/txnPantentInfoList.do");
        client.execute(httpGet);
        int veryCode=TesserocrUtil.imageRecog(client);
//        int veryCode=121;
        HttpPost post = new HttpPost("http://cpquery.sipo.gov.cn/freeze.main?txn-code=checkImgServlet");

        //装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("very-code", String.valueOf(veryCode)));
        //设置参数到请求对象中
        UrlEncodedFormEntity entity2 = new UrlEncodedFormEntity(nvps, "utf-8");
        post.setEntity(entity2);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        String resp = EntityUtils.toString(entity, "UTF-8");
        System.out.println("检查图片："+resp);
        if(!"0".equals(resp)){
            throw new Exception("图片异常");
        }
        httpGet=new HttpGet("http://cpquery.sipo.gov.cn/txnQueryOrdinaryPatents.do?select-key:shenqingh=&select-key:zhuanlimc=&select-key:shenqingrxm="+ enterpriseName+"&select-key:zhuanlilx=&select-key:shenqingr_from=&select-key:shenqingr_to=&verycode="+String.valueOf(veryCode)+"&inner-flag:open-type=window&inner-flag:flowno="+String.valueOf(new Date().getTime()));
        response = client.execute(httpGet);
        entity = response.getEntity();
        resp = EntityUtils.toString(entity, "UTF-8");
        return resp;
    }

    public int dealResp(CraCorpInfo craCorpInfo, ProxyDaXiang daXiang){
        try{
            String enterpriseName=craCorpInfo.getEnterpriseName();

            CloseableHttpClient client = HttpClientUtil.generateClient(daXiang);
            String content=getRespons( client,enterpriseName);
            Document doc = Jsoup.parse(content);
            int hasData=0;
            int result=2;
            int pageNum=insertDb(doc,enterpriseName,hasData);
            if(hasData>0){
                result=1;
            }
            for(int i=2;i<=pageNum;i++){
                String url="http://cpquery.sipo.gov.cn/txnQueryOrdinaryPatents.do?select-key:shenqingh=&select-key:zhuanlimc=&select-key:shenqingrxm="+enterpriseName+"&select-key:zhuanlilx=&select-key:shenqingr_from=&select-key:shenqingr_to=&very-code=&captchaNo=&fanyeflag=1&verycode=fanye&attribute-node:record_start-row="+String.valueOf(i*10+1-10)+"&attribute-node:record_page-row=10";
                System.out.println(url);
                HttpGet httpGet=new HttpGet(url);
                HttpResponse response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity, "UTF-8");
                doc = Jsoup.parse(content);
                try{
                    insertDb(doc,enterpriseName,hasData);
                }catch (Exception e){
                    
                }
            }
            return  result;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                daXiang=ProxyUtil.getProxy();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return 0;
        } catch (TesseractException e) {
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    private int insertDb(Document doc,String enterpriseName,int hasData){
        Elements elements=doc.getElementsByClass("content_listx_patent");
        for(Element element :elements){
            PrepertyRightInfo prepertyRightInfo=new PrepertyRightInfo();
            Elements etds=element.getElementsByTag("td");
            prepertyRightInfo.setRightType(etds.get(0).getElementsByAttributeValue("name","record:zhuanlilx").get(0).attr("title"));
            prepertyRightInfo.setRightNo(etds.get(1).getElementsByAttributeValue("name","record:shenqingh").get(0).html());
            prepertyRightInfo.setRightName(etds.get(2).getElementsByAttributeValue("name","record:zhuanlimc").get(0).attr("title"));
            prepertyRightInfo.setEnterpriseName(enterpriseName);
            prepertyRightInfo.setFilingDate(etds.get(4).getElementsByAttributeValue("name","record:shenqingr").get(0).attr("title"));
            prepertyRightInfo.setMainClassNo(etds.get(5).getElementsByAttributeValue("name","record:zhufenlh").get(0).attr("title"));
            hasData=prepertyRightInfoMapper.insert(prepertyRightInfo);
        }
        Elements pagination=doc.getElementsByClass("pagination");
        if(pagination.size()==0){
            return 1;
        }
        return new Integer(pagination.get(0).attr("data-totalpage"));
    }
}
