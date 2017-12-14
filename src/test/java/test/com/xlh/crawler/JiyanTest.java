package test.com.xlh.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlh.crawler.dto.ProxyDaXiang;
import com.xlh.crawler.utils.HttpClientUtil;
import com.xlh.crawler.utils.ProxyUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sun.tools.doclint.Entity.image;

public class JiyanTest {
    @Test
    public void test() throws Exception {
        ProxyDaXiang proxyDaXiang = new ProxyDaXiang();
        proxyDaXiang.setIp("110.172.220.194");
        proxyDaXiang.setPort("8080");
//        CloseableHttpClient client = HttpClientUtil.generateClient(ProxyUtil.getProxy());
        CloseableHttpClient client = HttpClientUtil.generateClient(null);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String v = String.valueOf(minute) + String.valueOf(second);
//        String resp = httpGet(client, "http://www.baidu.com?r=dd"+ URLEncoder.encode("|s","UTF-8"));
        String resp = httpGet(client, "http://www.gsxt.gov.cn/index.html");

        resp=httpGet(client,"http://www.gsxt.gov.cn/SearchItemCaptcha");
        JSONObject jsonObject= JSON.parseObject(resp);
        resp = httpGet(client, "http://www.gsxt.gov.cn/corp-query-custom-geetest-image.gif?v=" + v);
//        System.out.println(resp);

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        String jsFileName = "./expression.js";   // 读取js文件
        FileReader reader = new FileReader(jsFileName);   // 执行指定脚本
        engine.eval(reader);
        JSONArray jsonArray = JSONArray.parseArray(resp);
        Invocable invoke = (Invocable) engine;    // 调用merge方法，并传入两个参数
        String c = (String) invoke.invokeFunction("fromCharCode", jsonArray);
        System.out.println(c);
        Pattern r = Pattern.compile("location_info = (\\d+);");
        Matcher m = r.matcher(c);
        String location_info;
        if(m.find()){
            location_info=m.group(1);
        }else{
            throw new Exception("请求错误");
        }
        resp=httpGet(client,"http://www.gsxt.gov.cn/corp-query-geetest-validate-input.html?token="+location_info);
        jsonArray = JSONArray.parseArray(resp);
        c = (String) invoke.invokeFunction("fromCharCode", jsonArray);
        System.out.println(c);
        r = Pattern.compile("value: (\\d+)}");
        m = r.matcher(c);
        if(m.find()){
            location_info=m.group(1);
        }else{
            throw new Exception("请求错误");
        }
        int token=new Integer(location_info) ^ 536870911;
        resp=httpGet(client,"http://www.gsxt.gov.cn/corp-query-search-test.html?searchword=草地");
        System.out.println(resp);
        String challenge=jsonObject.getString("challenge");
        resp=httpGet(client,"http://jiyanapi.c2567.com/shibie?user=dandinglong&pass=aaa222&gt=1d2c042096e050f07cb35ff3df5afd92&challenge="+jsonObject.getString("challenge")+"&referer=http://www.gsxt.gov.cn&return=json&format=utf8");
        System.out.println(resp);
        jsonObject=JSON.parseObject(resp);

//        resp=httpGet(client,"http://www.gsxt.gov.cn/corp-query-search-1.html?tab=ent_tab&token="+String.valueOf(token)+"&searchword=草地&geetest_challenge="+challenge+"&geetest_validate="+jsonObject.getString("validate")+"&geetest_seccode="+jsonObject.getString("validate")+URLEncoder.encode("|jordan","UTF-8"));


//        http://www.gsxt.gov.cn/corp-query-search-1.html?tab=ent_tab&token=120953816&searchword=%E6%9D%A5%E4%BC%8A%E4%BB%BD&geetest_challenge=072dc3ae4366427c58463a14386c8e153j&geetest_validate=6419b2482ebd8d034e5a2ec99eab98c7&geetest_seccode=6419b2482ebd8d034e5a2ec99eab98c7|jordan
        HttpPost post=new HttpPost("http://www.gsxt.gov.cn/corp-query-search-1.html");
        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        post.setHeader("Content-Encoding", "gzip");
        post.setHeader("Accept-Language", "zh-CN");
        post.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("host", "www.gsxt.gov.cn");
        post.setHeader("Origin", "http://www.gsxt.gov.cn");
        post.setHeader("Referer", "http://www.gsxt.gov.cn/index.html");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("tab", "ent_tab"));
        nvps.add(new BasicNameValuePair("token", String.valueOf(token)));
        nvps.add(new BasicNameValuePair("searchword", "草地"));
        nvps.add(new BasicNameValuePair("geetest_challenge", jsonObject.getString("challenge")));
        nvps.add(new BasicNameValuePair("geetest_validate", jsonObject.getString("validate")));
        nvps.add(new BasicNameValuePair("geetest_seccode", jsonObject.getString("validate")+"|jordan"));
        //设置参数到请求对象中
        UrlEncodedFormEntity entity2 = new UrlEncodedFormEntity(nvps, "utf-8");
        post.setEntity(entity2);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        resp = EntityUtils.toString(entity, "UTF-8");
        System.out.println(resp);
//
//
//        tab=ent_tab&token=120953816&searchword=草地&geetest_challenge=072dc3ae4366427c58463a14386c8e153j&geetest_validate=6419b2482ebd8d034e5a2ec99eab98c7&geetest_seccode=6419b2482ebd8d034e5a2ec99eab98c7|jordan
////        /corp - query - geetest - validate - input.html ? token = " + location_info,
////        http://www.gsxt.gov.cn/corp-query-custom-geetest-image.gif?v=69
//        /corp-query-custom-geetest-image.gif?v=" + timestamp,
//
//        /corp-query-geetest-validate-input.html?token=
//
//
//
//        String gt="1d2c042096e050f07cb35ff3df5afd92";
//        String challeng="1ebde7be8aef9f5edcd5425a1c4b8dc7";
//        HttpGet get=new HttpGet("http://jiyanapi.c2567.com/shibie?user=dandinglong&pass=aaa222&gt=1d2c042096e050f07cb35ff3df5afd92&challenge=3d24c1f0fadaca5ba0b6399c64a4b53cio&referer=http%3A%2F%2Fwww.gsxt.gov.cn&model=3&return=json&format=utf8");
//
//        HttpResponse response = client.execute(get);
//        HttpEntity entity = response.getEntity();
//        String resp= EntityUtils.toString(entity,"UTF-8");
//        System.out.println(resp);
    }


    @Test
    public void calc_userresponse() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        String jsFileName = "./expression.js";   // 读取js文件
        FileReader reader = new FileReader(jsFileName);   // 执行指定脚本
        engine.eval(reader);
        String json = "[105,102,40,33,104,97,115,86,97,108,105,100,41,123,98,114,111,119,115,101,114,95,118,101,114,115,105,111,110,40,123,32,118,97,108,117,101,58,32,52,49,53,57,48,50,54,54,55,125,41,59,104,97,115,86,97,108,105,100,61,116,114,117,101,59,125]";
        JSONArray jsonArray = JSONArray.parseArray(json);
        if (engine instanceof Invocable) {
            Invocable invoke = (Invocable) engine;    // 调用merge方法，并传入两个参数
// c = merge(2, 3);
            String c = (String) invoke.invokeFunction("fromCharCode", jsonArray);
            System.out.println(c);
        }
    }

    private String httpGet(CloseableHttpClient client, String url) throws IOException {
        System.out.println(url);
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-Encoding", "gzip");
        get.setHeader("Accept-Language", "zh-CN");
        get.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        get.setHeader("Content-Security-Policy:", "script-src 'self' 'unsafe-eval' 'unsafe-inline' static.geetest.com api.geetest.com www.gsxt.gov.cn s4.cnzz.com c.cnzz.com hm.baidu.com");
        get.setHeader("Content-Type", "text/html;charset=UTF-8");
//        get.setHeader("Date", "text/html;charset=UTF-8");
        get.setHeader("Vary", "Accept-Encoding, Accept-Encoding");
        get.setHeader("X-Cache", "bypass");
        get.setHeader("X-Content-Type-Options", "nosniff");
        get.setHeader("X-XSS-Protection", "1; mode=block");
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String resp = EntityUtils.toString(entity, "UTF-8");
        return resp;
    }
}
