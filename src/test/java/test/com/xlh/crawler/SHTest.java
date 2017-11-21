package test.com.xlh.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xlh.crawler.Application;
import com.xlh.crawler.dto.CdmEntDtoCorpInfo;
import com.xlh.crawler.dto.CraCorpInfo;
import com.xlh.crawler.dto.ProxyDaXiang;
import com.xlh.crawler.dto.SqlLimit;
import com.xlh.crawler.exception.UserException;
import com.xlh.crawler.mapper.CdmEntDtoCorpInfoMapper;
import com.xlh.crawler.mapper.CraCorpInfoMapper;
import com.xlh.crawler.utils.HttpClientUtil;
import com.xlh.crawler.utils.ProxyUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import tk.mybatis.mapper.entity.Example;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class SHTest {
    private static Logger logger = LoggerFactory.getLogger(SHTest.class);
    @Autowired
    private CdmEntDtoCorpInfoMapper cdmEntDtoCorpInfoMapper;
    @Autowired
    private CraCorpInfoMapper craCorpInfoMapper;

    @Test
    public void mysql() throws Exception {
        Example example = new Example(CdmEntDtoCorpInfo.class);
        example.createCriteria().andLike("enterpriseName", "%上海%").andEqualTo("status", 0);
        int count = cdmEntDtoCorpInfoMapper.selectCountByExample(example);

        SqlLimit sqlLimit = new SqlLimit();
        int page = 0;
        int totalPage = count / 10;
        ProxyDaXiang proxyDaXiang;
        int returnsize = 0;
        while (page <= totalPage) {
            proxyDaXiang = ProxyUtil.getProxy();
            sqlLimit = new SqlLimit();
            sqlLimit.setStart(page * 10);
            sqlLimit.setLimit(10);
            List<CdmEntDtoCorpInfo> list = cdmEntDtoCorpInfoMapper.selectByPage(sqlLimit);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setStatus(1);
                try {
                    returnsize = test(list.get(i).getEnterpriseName(), proxyDaXiang);
                } catch (Exception e) {
                    logger.info("exception={}", e.getMessage());
                    try {
                        proxyDaXiang = ProxyUtil.getProxy();
                        returnsize = test(list.get(i).getEnterpriseName(), proxyDaXiang);
                    } catch (Exception e2) {
                        proxyDaXiang = ProxyUtil.getProxy();
                        list.get(i).setStatus(0);
                        logger.info("exception={}", e.getMessage());
                        continue;
                    }
                }
                if (returnsize == 0) {
                    list.get(i).setStatus(2);
                }
                cdmEntDtoCorpInfoMapper.updateByPrimaryKey(list.get(i));
                logger.info("更新了数据id为{}", list.get(i).getId());
            }
            page++;
        }


    }


    private int test(String keyword, ProxyDaXiang proxyDaXiang) throws Exception {

        logger.info("keyword={} proxyDaXiang={}", keyword, JSON.toJSONString(proxyDaXiang));

        CloseableHttpClient client= HttpClientUtil.generateClient(proxyDaXiang);


        HttpGet get = new HttpGet("http://sh.gsxt.gov.cn/notice/");
        get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,zh-TW;q=0.2");
        get.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        String pattern = "\"session\\.token\": \"(.*)\"";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(result);
        String token;
        if (m.find()) {
            token = m.group(1);
        } else {
            throw new Exception("not found token");
        }
        logger.info("{} step 1 finish", keyword);

//        System.out.println(token);

        get = new HttpGet("http://sh.gsxt.gov.cn/notice/pc-geetest/register?v=" + String.valueOf((new Date()).getTime()));
        get.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,zh-TW;q=0.2");
        get.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        get.setHeader("Referer", "http://sh.gsxt.gov.cn/notice/");
        get.setHeader("X-Requested-With", "XMLHttpRequest");
        response = client.execute(get);
        entity = response.getEntity();
        result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(result);
//        System.out.println(result);
        logger.info("{} step 2 finish", keyword);

        get = new HttpGet("http://sh.gsxt.gov.cn/notice/security/verify_ip");

        get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,zh-TW;q=0.2");
        get.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        get.setHeader("Referer", "http://sh.gsxt.gov.cn/notice/");
        get.setHeader("X-Requested-With", "XMLHttpRequest");
        get.setHeader("Origin", "http://sh.gsxt.gov.cn");
        response = client.execute(get);
        entity = response.getEntity();
        result = EntityUtils.toString(entity, "UTF-8");
        logger.info("{} step 3 finish", keyword);
//        System.out.println(result);

//        HttpPost post=new HttpPost("http://localhost:8080/test");
        HttpPost post = new HttpPost("http://sh.gsxt.gov.cn/notice/security/verify_keyword");
        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,zh-TW;q=0.2");
        post.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        post.setHeader("Referer", "http://sh.gsxt.gov.cn/notice/");
        post.setHeader("X-Requested-With", "XMLHttpRequest");
        post.setHeader("Origin", "http://sh.gsxt.gov.cn");
        //装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("keyword", keyword));
        //设置参数到请求对象中
        UrlEncodedFormEntity entity2 = new UrlEncodedFormEntity(nvps, "utf-8");
        post.setEntity(entity2);
        response = client.execute(post);
        entity = response.getEntity();
        result = EntityUtils.toString(entity, "UTF-8");
        logger.info("{} step 4 finish", keyword);
//        System.out.println(result);

        String validate = calc_validate(jsonObject.getString("challenge"));

        post = new HttpPost("http://sh.gsxt.gov.cn/notice/pc-geetest/validate");
        post.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,zh-TW;q=0.2");
        post.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        post.setHeader("Referer", "http://sh.gsxt.gov.cn/notice/");
        post.setHeader("X-Requested-With", "XMLHttpRequest");
        post.setHeader("Origin", "http://sh.gsxt.gov.cn");
        //装填参数
        nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("geetest_challenge", jsonObject.getString("challenge")));
        nvps.add(new BasicNameValuePair("geetest_validate", validate));
        nvps.add(new BasicNameValuePair("geetest_seccode", validate + "|jordan"));
        //设置参数到请求对象中
        entity2 = new UrlEncodedFormEntity(nvps, "utf-8");
        post.setEntity(entity2);
        response = client.execute(post);
        entity = response.getEntity();
        result = EntityUtils.toString(entity, "UTF-8");
        logger.info("{} step 5 finish", keyword);
//        System.out.println(result);

        post = new HttpPost("http://sh.gsxt.gov.cn/notice/search/ent_info_list");
        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,zh-TW;q=0.2");
        post.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        post.setHeader("Referer", "http://sh.gsxt.gov.cn/notice/");
        post.setHeader("X-Requested-With", "XMLHttpRequest");
        post.setHeader("Origin", "http://sh.gsxt.gov.cn");
        nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("condition.searchType", "1"));
        nvps.add(new BasicNameValuePair("captcha", ""));
        nvps.add(new BasicNameValuePair("geetest_challenge", jsonObject.getString("challenge")));
        nvps.add(new BasicNameValuePair("geetest_validate", validate));
        nvps.add(new BasicNameValuePair("geetest_seccode", validate + "|jordan"));
        nvps.add(new BasicNameValuePair("condition.keyword", keyword));
        nvps.add(new BasicNameValuePair("session.token", token));
        //设置参数到请求对象中
        post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        response = client.execute(post);
        entity = response.getEntity();
        result = EntityUtils.toString(entity, "UTF-8");
        logger.info("{} step 6 finish", keyword);

        Document doc = Jsoup.parse(result);
        Elements elements = doc.body().getElementsByClass("contentA1");
        Element contentA1 = elements.get(0);
        logger.info("total string ={}", contentA1.getElementsByTag("p").html());
        if (contentA1 == null) {
            throw new Exception("not found contentA1");
        }
        Elements tableContents = contentA1.getElementsByClass("tableContent");
        for (int i = 0; i < tableContents.size(); i++) {
            CraCorpInfo craCorpInfo = new CraCorpInfo();
            craCorpInfo.setUrl(tableContents.get(i).attr("onclick").replace("window.open('", "").replace("')", ""));
            Elements tds = tableContents.get(i).getElementsByTag("td");
            String td0Html = tds.get(0).html();
            int index = td0Html.indexOf("<i class=\"iconDefault");
            craCorpInfo.setEnterpriseName(td0Html.substring(0, index).replace("<span style=\"color: red;\">", "").replace("</span>", "").trim());
            Elements ths = tableContents.get(i).getElementsByClass("icon1");
            craCorpInfo.setCreditNo(ths.get(0).getElementsByTag("em").html());
            logger.info("craCorpInfo={}", JSON.toJSONString(craCorpInfo));
            try {

                craCorpInfoMapper.insert(craCorpInfo);
            } catch (Exception e) {

            }
        }
        return tableContents.size();

    }

    private String calc_validate(String challenge) throws Exception {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> list1 = new ArrayList<String>();
        list1.add("186");
        list1.add("1");
        list1.add("98");
        list.add(list1);
        list1 = new ArrayList<String>();
        list1.add("82");
        list1.add("0");
        list1.add("136");
        list.add(list1);
        list1 = new ArrayList<String>();
        list1.add("61");
        list1.add("5");
        list1.add("108");
        list.add(list1);
        list1 = new ArrayList<String>();
        list1.add("128");
        list1.add("2");
        list1.add("7");
        list.add(list1);
        list1 = new ArrayList<String>();
        list1.add("130");
        list1.add("4");
        list1.add("99");
        list.add(list1);
        list1 = new ArrayList<String>();
        list1.add("189");
        list1.add("3");
        list1.add("65");
        list.add(list1);
        list1 = new ArrayList<String>();
        list1.add("108");
        list1.add("5");
        list1.add("285");
        list.add(list1);
        list1 = new ArrayList<String>();
        list1.add("136");
        list1.add("0");
        list1.add("36");
        list.add(list1);
        list1 = new ArrayList<String>();
        list1.add("41");
        list1.add("0");
        list1.add("263");
        list.add(list1);
        list1 = new ArrayList<String>();
        list1.add("124");
        list1.add("3");
        list1.add("185");
        list.add(list1);
        Random random = new Random();
        String distance, rand0, rand1;
        list1 = list.get(random.nextInt(list.size()));
        distance = list1.get(0);
        rand0 = list1.get(1);
        rand1 = list1.get(2);
        String distance_r = calc_userresponse(distance, challenge);
        String rand0_r = calc_userresponse(rand0, challenge);
        String rand1_r = calc_userresponse(rand1, challenge);
        String validate = distance_r + '_' + rand0_r + '_' + rand1_r;
        return validate;
    }

    private String calc_userresponse(String distance, String challenge) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        String jsFileName = "./expression.js";   // 读取js文件
        FileReader reader = new FileReader(jsFileName);   // 执行指定脚本
        engine.eval(reader);
        if (engine instanceof Invocable) {
            Invocable invoke = (Invocable) engine;    // 调用merge方法，并传入两个参数
// c = merge(2, 3);
            String c = (String) invoke.invokeFunction("userresponse", distance, challenge);
            return c;
        }
        throw new Exception("js failed");
    }


}
