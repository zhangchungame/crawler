package test.com.xlh.crawler;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PropertyRightTest {
    @Test
    public void test() throws IOException {
        ProxyDaXiang daXiang= ProxyUtil.getProxy();
        CloseableHttpClient client= HttpClientUtil.generateClient(daXiang);
        HttpGet get = new HttpGet("http://2017.ip138.com/ic.asp");

        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "gb2312");
        System.out.println(result);
        HttpGet httpGet=new HttpGet("http://cpquery.sipo.gov.cn/freeze.main?txn-code=createImgServlet&freshStept=1");
         response = client.execute(httpGet);
         entity = response.getEntity();
        InputStream in = entity.getContent();
        String fileName=String.valueOf((new Date()).getTime());
        File file = new File("d:/logs/"+fileName+".jpg");
        FileOutputStream fout = new FileOutputStream(file);
        int l;
        byte[] tmp = new byte[1024];
        while ((l = in.read(tmp)) != -1) {
            fout.write(tmp, 0, l);
            // 注意这里如果用OutputStream.write(buff)的话，图片会失真
        }
        // 将文件输出到本地
        fout.flush();
        EntityUtils.consume(entity);
    }
}
