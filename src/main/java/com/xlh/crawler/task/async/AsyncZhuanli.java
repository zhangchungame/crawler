package com.xlh.crawler.task.async;

import com.alibaba.fastjson.JSON;
import com.xlh.crawler.dto.*;
import com.xlh.crawler.mapper.CdmEntDtoCorpInfoMapper;
import com.xlh.crawler.mapper.CraCorpInfoMapper;
import com.xlh.crawler.mapper.PrepertyRightInfoMapper;
import com.xlh.crawler.service.IpPoolService;
import com.xlh.crawler.utils.HttpClientUtil;
import com.xlh.crawler.utils.ProxyUtil;
import com.xlh.crawler.utils.ShZhenXinThread;
import com.xlh.crawler.utils.TesserocrUtil;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
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
import org.apache.ibatis.session.RowBounds;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AsyncZhuanli {
    private static Logger logger = LoggerFactory.getLogger(AsyncZhuanli.class);

    @Autowired
    private IpPoolService ipPoolService;

    @Autowired
    private CraCorpInfoMapper craCorpInfoMapper;
    @Autowired
    private PrepertyRightInfoMapper prepertyRightInfoMapper;


    public String getRespons(HttpClient client, String enterpriseName) throws Exception {
        HttpGet httpGet = new HttpGet("http://cpquery.sipo.gov.cn/txnPantentInfoList.do");
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String resp = EntityUtils.toString(entity, "UTF-8");
        if (resp.equals("")) {
            throw new Exception("返回空值");
        }
        ZhuanliVerificationCode zhuanliVerificationCode=imageRecog(client,0);
        System.out.println("zhuanliVerificationCode======================"+JSON.toJSONString(zhuanliVerificationCode));
        String pattern = "(\\d)(\\+|\\-)(\\d)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(zhuanliVerificationCode.getCode());
        int veryCode;
        int first = new Integer(m.group(1));
        int sec = new Integer(m.group(3));
        if (m.group(2).equals("+")) {
            veryCode = first + sec;
        } else {
            veryCode = first - sec;
        }
//        int veryCode=121;
        HttpPost post = new HttpPost("http://cpquery.sipo.gov.cn/freeze.main?txn-code=checkImgServlet");

        //装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("very-code", String.valueOf(veryCode)));
        //设置参数到请求对象中
        UrlEncodedFormEntity entity2 = new UrlEncodedFormEntity(nvps, "utf-8");
        post.setEntity(entity2);
        response = client.execute(post);
        entity = response.getEntity();
        resp = EntityUtils.toString(entity, "UTF-8");
        System.out.println("检查图片：" + resp);
        if (!"0".equals(resp)) {
            throw new Exception("图片异常");
        }
        httpGet = new HttpGet("http://cpquery.sipo.gov.cn/txnQueryOrdinaryPatents.do?select-key:shenqingh=&select-key:zhuanlimc=&select-key:shenqingrxm=" + enterpriseName + "&select-key:zhuanlilx=&select-key:shenqingr_from=&select-key:shenqingr_to=&verycode=" + String.valueOf(veryCode) + "&inner-flag:open-type=window&inner-flag:flowno=" + String.valueOf(new Date().getTime()));
        response = client.execute(httpGet);
        entity = response.getEntity();
        resp = EntityUtils.toString(entity, "UTF-8");
        return resp;
    }

    @Async("taskExecutorZhuanli")
    public void dealResp(CraCorpInfo craCorpInfo, ProxyDaXiang daXiang, Semaphore semaphore) {
        try {
            String enterpriseName = craCorpInfo.getEnterpriseName();

            CloseableHttpClient client = HttpClientUtil.generateClient(daXiang);
            String content = getRespons(client, enterpriseName);
            Document doc = Jsoup.parse(content);

            List<PrepertyRightInfo> list = new ArrayList<>();
            int result = 2;
            int pageNum = insertDb(doc, enterpriseName, list);
            if (list.size() > 0) {
                result = 1;
            }
            for (int i = 2; i <= pageNum; i++) {
                String url = "http://cpquery.sipo.gov.cn/txnQueryOrdinaryPatents.do?select-key:shenqingh=&select-key:zhuanlimc=&select-key:shenqingrxm=" + enterpriseName + "&select-key:zhuanlilx=&select-key:shenqingr_from=&select-key:shenqingr_to=&very-code=&captchaNo=&fanyeflag=1&verycode=fanye&attribute-node:record_start-row=" + String.valueOf(i * 10 + 1 - 10) + "&attribute-node:record_page-row=10";
                System.out.println(url);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity, "UTF-8");
                doc = Jsoup.parse(content);
                insertDb(doc, enterpriseName, list);
            }
            for (PrepertyRightInfo item : list) {
                item.setEnterpriseId(craCorpInfo.getId());
                prepertyRightInfoMapper.insert(item);
            }
            craCorpInfo.setRightStatus(result);
            logger.info("craCorpInfo={}", JSON.toJSONString(craCorpInfo));
            craCorpInfoMapper.updateByPrimaryKey(craCorpInfo);
        } catch (Exception e) {
            logger.info("daxiang={}    exception={}", JSON.toJSONString(daXiang),e.getMessage());
        }
        semaphore.release();
    }

    private int insertDb(Document doc, String enterpriseName, List<PrepertyRightInfo> list) {
        Elements elements = doc.getElementsByClass("content_listx_patent");
        for (Element element : elements) {
            PrepertyRightInfo prepertyRightInfo = new PrepertyRightInfo();
            Elements etds = element.getElementsByTag("td");
            prepertyRightInfo.setRightType(etds.get(0).getElementsByAttributeValue("name", "record:zhuanlilx").get(0).attr("title"));
            prepertyRightInfo.setRightNo(etds.get(1).getElementsByAttributeValue("name", "record:shenqingh").get(0).html());
            prepertyRightInfo.setRightName(etds.get(2).getElementsByAttributeValue("name", "record:zhuanlimc").get(0).attr("title"));
            prepertyRightInfo.setEnterpriseName(enterpriseName);
            prepertyRightInfo.setFilingDate(etds.get(4).getElementsByAttributeValue("name", "record:shenqingr").get(0).attr("title"));
            prepertyRightInfo.setMainClassNo(etds.get(5).getElementsByAttributeValue("name", "record:zhufenlh").get(0).attr("title"));
//            prepertyRightInfoMapper.insert(prepertyRightInfo);
            list.add(prepertyRightInfo);
        }
        Elements pagination = doc.getElementsByClass("pagination");
        if (pagination.size() == 0) {
            return 1;
        }
        return new Integer(pagination.get(0).attr("data-totalpage"));
    }


    private ZhuanliVerificationCode imageRecog(HttpClient client,int times) throws Exception {
        ZhuanliVerificationCode result=new ZhuanliVerificationCode();
        if(times>10){
            throw new Exception("超过10次未识别");
        }
        ITesseract instance = new Tesseract();
        HttpGet httpGet = new HttpGet("http://cpquery.sipo.gov.cn/freeze.main?txn-code=createImgServlet&freshStept=1");
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        String fileName = String.valueOf((new Date()).getTime());
        File file = new File("d:/logs/yanzhengma/" + fileName + ".jpg");
//        File file = new File("/home/zc/tmpfile/"+fileName+".jpg");
        FileOutputStream fout = new FileOutputStream(file);
        int l;
        byte[] tmp = new byte[1024];
        int line = 0;
        while ((l = in.read(tmp)) != -1) {
            fout.write(tmp, 0, l);
            line++;
            // 注意这里如果用OutputStream.write(buff)的话，图片会失真
        }
        // 将文件输出到本地
        fout.flush();
        EntityUtils.consume(entity);
        instance.setDatapath("C:/Program Files (x86)/Tesseract-OCR/tessdata");
//        instance.setDatapath("/usr/share/tesseract-ocr/tessdata");
        String str = instance.doOCR(file);
        str=str.replace("-","-").replace("—","-");
        logger.info("文件：" + fileName + "识别结果：" + str);
        String pattern = "(\\d)(\\+|\\-)(\\d)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        if (m.find()) {
//            int first = new Integer(m.group(1));
//            int sec = new Integer(m.group(3));
//            if (m.group(2).equals("+")) {
//                result = first + sec;
//            } else {
//                result = first - sec;
//            }
            result.setCode(str);
            result.setFileName(fileName+".jpg");
            return result;
        } else {
            times++;
            return imageRecog(client,times);
        }
    }
}
