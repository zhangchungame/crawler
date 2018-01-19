package test.com.xlh.crawler;

import com.alibaba.fastjson.JSONObject;
import com.xlh.crawler.utils.HttpClientUtil;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GsxtTest {

    @Test
    public void test() throws Exception {
        CloseableHttpClient client = HttpClientUtil.generateClient(null);
        HttpGet get = new HttpGet("http://www.gsxt.gov.cn/SearchItemCaptcha");
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(result);
        String gt = jsonObject.getString("gt");
        String challenge = jsonObject.getString("challenge");
        get = new HttpGet("http://jiyanapi.c2567.com/shibie?gt=" + gt + "&challenge=" + challenge + "&referer=http://www.gsxt.gov.cn&user=dandinglong&pass=aaa222&return=json&format=utf8");
        response = client.execute(get);
        entity = response.getEntity();
        result = EntityUtils.toString(entity, "UTF-8");
        jsonObject=JSONObject.parseObject(result);
        if(!jsonObject.getString("status").equals("ok")){
            throw new Exception("cuowuo");
        }
        HttpPost post = new HttpPost("http://sh.gsxt.gov.cn/notice/security/verify_keyword");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("tab", "ent_tab"));
        nvps.add(new BasicNameValuePair("token", "121003608"));
        nvps.add(new BasicNameValuePair("searchword", "上海信隆行"));
        nvps.add(new BasicNameValuePair("geetest_challenge", challenge));
        nvps.add(new BasicNameValuePair("geetest_validate", jsonObject.getString("validate")));
        nvps.add(new BasicNameValuePair("searchword", jsonObject.getString("validate")+"|jordan"));
        //设置参数到请求对象中
        UrlEncodedFormEntity entity2 = new UrlEncodedFormEntity(nvps, "utf-8");
        post.setEntity(entity2);
        response = client.execute(post);
        entity = response.getEntity();
        result = EntityUtils.toString(entity, "UTF-8");
        System.out.println(result);
    }
}
