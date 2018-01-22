package test.com.xlh.crawler;

import com.xlh.crawler.utils.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class SearchItemTest {

    @Test
    public void test() throws IOException {
        HttpClientContext context = HttpClientContext.create();
        CloseableHttpClient client = HttpClientUtil.generateClient(null);
        HttpGet get = new HttpGet("http://www.gsxt.gov.cn/SearchItemCaptcha");
        get.setHeader("Host", "www.gsxt.gov.cn");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0");
        get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        get.setHeader("Accept-Encoding", "gzip, deflate");
        get.setHeader("Referer", "http://www.gsxt.gov.cn/SearchItemCaptcha");
        get.setHeader("Connection", "http://www.gsxt.gov.cn/SearchItemCaptcha");
        get.setHeader("Upgrade-Insecure-Requests", "1");
        get.setHeader("Cache-Control", "max-age=0, no-cache");
        CloseableHttpResponse response = client.execute(get, context);
        HttpEntity entity = response.getEntity();
        String resp = EntityUtils.toString(entity, "UTF-8");
        System.out.println(resp);
        CookieStore cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
            for (Cookie cookie : cookies) {
                System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
            }

    }
}
