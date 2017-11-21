package test.com.xlh.crawler;

import com.xlh.crawler.utils.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PropertyRightTest {
    @Test
    public void test() throws IOException {

        CloseableHttpClient client= HttpClientUtil.generateClient(null);
        HttpPost post = new HttpPost("http://di.izhiliao.com.cn/api/patent/search/expression");
        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,zh-TW;q=0.2");
        post.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        post.setHeader("X-Requested-With", "XMLHttpRequest");
        //装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("express", "(申请人 =  ( 上海杏子餐饮管理有限公司 ) )"));
        nvps.add(new BasicNameValuePair("page", "1"));
        nvps.add(new BasicNameValuePair("sort_column", "-RELEVANCE"));
        nvps.add(new BasicNameValuePair("pdb", ""));
        nvps.add(new BasicNameValuePair("union_search", "false"));
        //设置参数到请求对象中
        UrlEncodedFormEntity entity2 = new UrlEncodedFormEntity(nvps, "utf-8");
        post.setEntity(entity2);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        System.out.println(result);
    }
}
