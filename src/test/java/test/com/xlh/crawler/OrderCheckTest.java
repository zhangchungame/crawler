package test.com.xlh.crawler;

import com.xlh.crawler.utils.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

public class OrderCheckTest {
    @Test
    public void test() throws IOException {
//        CloseableHttpClient client = HttpClientUtil.generateClient(null);
//        BigDecimal orderNum=new BigDecimal("500000000000000");
//        while (true){
//            HttpGet get = new HttpGet("http://tvp.daxiangdaili.com/ip/?tid="+orderNum.toString()+"&num=1");
//            HttpResponse response = client.execute(get);
//            HttpEntity entity = response.getEntity();
//            String result = EntityUtils.toString(entity, "UTF-8");
//        }
    }

}
