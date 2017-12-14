package test.com.xlh.crawler;

import com.alibaba.fastjson.JSONArray;
import com.xlh.crawler.dto.ProxyDaXiang;
import com.xlh.crawler.utils.HttpClientUtil;
import com.xlh.crawler.utils.ProxyUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static com.sun.tools.doclint.Entity.image;

public class JiyanTest {
    @Test
    public void test() throws IOException, ScriptException, NoSuchMethodException {
        ProxyDaXiang proxyDaXiang=new ProxyDaXiang();
        proxyDaXiang.setIp("110.172.220.194");
        proxyDaXiang.setPort("8080");
        CloseableHttpClient client= HttpClientUtil.generateClient(ProxyUtil.getProxy());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int minute=calendar.get(Calendar.MINUTE);
        int second=calendar.get(Calendar.SECOND);
        String v=String.valueOf(minute)+String.valueOf(second);
        String resp=httpGet(client,"http://www.baidu.com");
        resp=httpGet(client,"http://www.gsxt.gov.cn/index.html");
        System.out.println(resp);
        resp=httpGet(client,"http://www.gsxt.gov.cn/corp-query-custom-geetest-image.gif?v="+v);
//        System.out.println(resp);

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        String jsFileName = "./expression.js";   // 读取js文件
        FileReader reader = new FileReader(jsFileName);   // 执行指定脚本
        engine.eval(reader);
        JSONArray jsonArray=JSONArray.parseArray(resp);
        if (engine instanceof Invocable) {
            Invocable invoke = (Invocable) engine;    // 调用merge方法，并传入两个参数
// c = merge(2, 3);
            String c = (String) invoke.invokeFunction("fromCharCode", jsonArray);
            System.out.println(c);
        }
//        http://www.gsxt.gov.cn/corp-query-custom-geetest-image.gif?v=69
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
        String json="[105,102,40,33,104,97,115,86,97,108,105,100,41,123,98,114,111,119,115,101,114,95,118,101,114,115,105,111,110,40,123,32,118,97,108,117,101,58,32,52,49,53,57,48,50,54,54,55,125,41,59,104,97,115,86,97,108,105,100,61,116,114,117,101,59,125]";
        JSONArray jsonArray=JSONArray.parseArray(json);
        if (engine instanceof Invocable) {
            Invocable invoke = (Invocable) engine;    // 调用merge方法，并传入两个参数
// c = merge(2, 3);
            String c = (String) invoke.invokeFunction("fromCharCode", jsonArray);
            System.out.println(c);
        }
    }

    private String httpGet(CloseableHttpClient client,String url) throws IOException {
        HttpGet get=new HttpGet(url);
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
        String resp= EntityUtils.toString(entity,"UTF-8");
        return resp;
    }
}
