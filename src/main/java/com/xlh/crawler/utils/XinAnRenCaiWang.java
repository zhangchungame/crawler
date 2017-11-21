//package com.xlh.crawler.utils;
//
//import org.apache.commons.httpclient.Cookie;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.NameValuePair;
//import org.apache.commons.httpclient.cookie.CookiePolicy;
//import org.apache.commons.httpclient.cookie.CookieSpec;
//import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//import java.io.IOException;
//
//public class XinAnRenCaiWang {
//    private static final String SITE = "www.qixin.com";
//    private static final int PORT = 80;
//    private static final String loginAction = "/search?key=%E6%90%9E%E5%AE%9A&page=1";
//    private static final String forwardURL =
//            "http://user.goodjobs.cn/dispatcher.php/module/Personal/";
//    private static final String toUrl = "d:\\jsoup_test\\";
//    private static final String hostCss = "d:\\jsoup_test\\style.txt";
//    private static final String Img = "http://user.goodjobs.cn/images";
//    private static final String _JS = "http://user.goodjobs.cn/scripts/fValidate/fValidate.one.js";
//
//    /**
//     * 模拟等录
//     *
//     * @param LOGON_SITE
//     * @param LOGON_PORT
//     * @param login_Action
//     * @param params
//     * @throws Exception
//     */
//    private static String[] loginHtml(String LOGON_SITE, int LOGON_PORT, String
//            login_Action, String... params) throws Exception {
//        String[] result = null;
//        HttpClient client = new HttpClient();
//        client.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);
//// 模拟登录页面
//
//
//        GetMethod get=new GetMethod(login_Action);
//        client.executeMethod(get);
////        System.out.println("执行状态：" + client.getState());
//        System.out.println("执行状态：" + get.getResponseBodyAsString());
//
//        get.releaseConnection();
//        PostMethod post = new PostMethod("/api/user/login");
//        NameValuePair userName = new NameValuePair("memberName", params[0]);
//        NameValuePair password = new NameValuePair("password", params[1]);
//        post.setRequestBody(new NameValuePair[]{userName, password});
//// 查看 cookie 信息
//        CookieSpec cookiespec = CookiePolicy.getDefaultSpec();
//        Cookie[] cookies = cookiespec.match(LOGON_SITE, LOGON_PORT, "/", false,
//                client.getState().getCookies());
//        client.getState()
//        if (cookies != null)
//            if (cookies.length == 0) {
//                System.out.println("Cookies is not Exists ");
//            } else {
//                for (int i = 0; i < cookies.length; i++) {
////                    System.out.println("----------------------------------------------------");
////                    System.out.println(cookies[i].toString());
//                    result = cookies[i].toString().split("=");
////                    System.out.println("----------------------------------------------------");
//                }
//            }
//
//        return result;
//    }
//
//    /**
//     * @param cookies
//     * @return
//     */
//    public static Document getHtmlDocument(String[] cookies) {
//        try {
//            Document doc = Jsoup.connect(forwardURL).cookie(cookies[0], cookies[1]).get();
//            return doc;
//        } catch (IOException e) {
//            System.out.println("页面获取异常！");
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static void main(String[] args) {
//        String[] params = {"job17093123661", "2534133662qq"};
//        String[] strings = null;
//        try {
//            strings = loginHtml(SITE, PORT, loginAction, params);
//            if (strings.length > 0) {
//                Document htmlDocument = getHtmlDocument(strings);
//                System.out.println(htmlDocument);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//}
