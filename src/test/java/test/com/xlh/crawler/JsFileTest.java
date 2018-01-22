package test.com.xlh.crawler;

import com.xlh.crawler.dto.ProxyDaXiang;
import com.xlh.crawler.utils.HttpClientUtil;
import com.xlh.crawler.utils.ProxyUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsFileTest {
    @Test
    public void test() throws IOException, ScriptException, NoSuchMethodException {
//        ProxyDaXiang proxyDaXiang= ProxyUtil.getProxy();
//        System.out.println(proxyDaXiang);
//        if(1==1){
//            return;
//        }
        CloseableHttpClient client = HttpClientUtil.generateClient(null);
//        HttpGet get=new HttpGet("http://www.gsxt.gov.cn/SearchItemCaptcha");
        HttpClientContext context = HttpClientContext.create();
        HttpGet httpget = new HttpGet("http://www.gsxt.gov.cn/SearchItemCaptcha");

        CookieStore cookieStore=new BasicCookieStore();
        BasicClientCookie cookie1 = new BasicClientCookie("__jsluid", "08940aa083a8fdaba71bd82dc4db3474");
        cookieStore.addCookie(cookie1);

        BasicClientCookie cookie2 = new BasicClientCookie("__jsl_clearance", "1516608719.103|0|9IBYX7J9kvOS13KqcvOBJcAW9j4%3D");
        cookieStore.addCookie(cookie2);
//        context.setCookieStore(cookieStore);
        CloseableHttpResponse response = client.execute(httpget, context);
        String resp;
        try {
//            cookieStore = context.getCookieStore();
//            cookieStore.clear();
//            cookieStore.addCookie(cookie);
//            cookieStore.addCookie(cookie2);
            List<Cookie> cookies = cookieStore.getCookies();
            for (Cookie cookie : cookies) {
                System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
            }
            HttpEntity httpEntity = response.getEntity();
            resp = EntityUtils.toString(httpEntity, "UTF-8");

        } finally {
            response.close();
        }
        response = client.execute(httpget, context);
        try {
            context.setCookieStore(cookieStore);
//            List<Cookie> cookies = cookieStore.getCookies();
//            for (Cookie cookie : cookies) {
//                System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
//            }
            HttpEntity httpEntity = response.getEntity();
            resp = EntityUtils.toString(httpEntity, "UTF-8");

        } finally {
            response.close();
        }

//        HttpResponse response=client.execute(get);

        resp = resp.substring(8);
        String tmp[] = resp.split("</script");
        resp = tmp[0];
        resp = resp.replace("eval(y.replace", "var aaa=(y.replace");
        resp = resp + "aaa=aaa.replace(\"while(window._phantom||window.__phantomas){};\",\"\");bbb=aaa.split(\"setTimeout\");\n" +
                "    aaa=bbb[0]+\"return dc;}}\";\n" +
                "    aaa=aaa.replace(\"var l=\",\"{fa:\");\n" +
                "  var ffa=eval(\"(\"+aaa+\")\");\n" +
                "    var fffa=ffa.fa();";
        System.out.println(resp);


        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

        String script = resp;
        scriptEngine.eval(script);
        String bbb = (String) scriptEngine.get("fffa");

        System.out.println(bbb);

        String tmp3[] = bbb.split("=");

//        BasicClientCookie cookie = new BasicClientCookie(tmp3[0], URLDecoder.decode( tmp3[1],"UTF-8"));
//        cookie.setVersion(0);
//        cookie.setDomain("www.gsxt.gov.cn");
//        cookie.setPath("/");
//        cookie.setAttribute("path","/");
//        cookie.setAttribute("max-age","31536000");
//        cookie.setAttribute("httponly",null);
//        // cookie.setAttribute(ClientCookie.VERSION_ATTR, "0");
//        // cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "127.0.0.1");
//        // cookie.setAttribute(ClientCookie.PORT_ATTR, "8080");
//        // cookie.setAttribute(ClientCookie.PATH_ATTR, "/CwlProWeb");
//        cookieStore.addCookie(cookie);
//
//        httpget = new HttpGet("http://www.gsxt.gov.cn/SearchItemCaptcha");
//        context.setCookieStore(cookieStore);
//         response = client.execute(httpget, context);
//
//        try {
//            cookieStore = context.getCookieStore();
//            List<Cookie> cookies = cookieStore.getCookies();
//            for (Cookie cookie2 : cookies) {
//                System.out.println("key:" + cookie2.getName() + "  value:" + cookie2.getValue());
//            }
//            HttpEntity httpEntity = response.getEntity();
//            resp = EntityUtils.toString(httpEntity, "UTF-8");
//
//        } finally {
//            response.close();
//        }
    }
}
