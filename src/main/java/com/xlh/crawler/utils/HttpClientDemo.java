package com.xlh.crawler.utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;

public class HttpClientDemo
{

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception
    {
        getResoucesByLoginCookies();
    }

    /**
     * 根据登录Cookie获取资源
     * 一切异常均未处理，需要酌情检查异常
     *
     * @throws Exception
     */
    private static void getResoucesByLoginCookies() throws Exception
    {
        HttpClientDemo demo = new HttpClientDemo();
        String username = "XXXXXXXXX";// 登录用户
        String password = "XXXXXXXX";// 登录密码

        // 需要提交登录的信息
        String urlLogin = "http://crm.easyrong.net/login?memberPhone=13681736848&memberPwd=xlh123456";

        // 登录成功后想要访问的页面 可以是下载资源 需要替换成自己的iteye Blog地址
        String urlAfter = "http://crm.easyrong.net/crm/user/viewall";

        DefaultHttpClient client = new DefaultHttpClient(new PoolingClientConnectionManager());

        /**
         * 第一次请求登录页面 获得cookie
         * 相当于在登录页面点击登录，此处在URL中 构造参数，
         * 如果参数列表相当多的话可以使用HttpClient的方式构造参数
         * 此处不赘述
         */
        HttpPost post = new HttpPost(urlLogin);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        CookieStore cookieStore = client.getCookieStore();
        Cookie cookie =new BasicClientCookie("sid","s%3Ac8fszQoKd9WC_JcNRESYZBwtc4NVW0wh.hwFzog8pTjIDURf%2BStsbiGa%2BPeNnVPjs4J6Fx9gzi7Y");
        cookieStore.addCookie(cookie);
        client.setCookieStore(cookieStore);

        /**
         * 带着登录过的cookie请求下一个页面，可以是需要登录才能下载的url
         * 此处使用的是iteye的博客首页，如果登录成功，那么首页会显示【欢迎XXXX】
         *
         */
        HttpGet get = new HttpGet(urlAfter);
        response = client.execute(get);
        entity = response.getEntity();

        /**
         * 将请求结果放到文件系统中保存为 myindex.html,便于使用浏览器在本地打开 查看结果
         */

        String pathName = "d:\\myindex.html";
        writeHTMLtoFile(entity, pathName);
    }

    /**
     * Write htmL to file.
     * 将请求结果以二进制形式放到文件系统中保存为.html文件,便于使用浏览器在本地打开 查看结果
     *
     * @param entity the entity
     * @param pathName the path name
     * @throws Exception the exception
     */
    public static void writeHTMLtoFile(HttpEntity entity, String pathName) throws Exception
    {

//        byte[] bytes = new byte[(int) entity.getContentLength()];

        FileOutputStream fos = new FileOutputStream(pathName);

        byte[] bytes = EntityUtils.toByteArray(entity);

        fos.write(bytes);

        fos.flush();

        fos.close();
    }

}
