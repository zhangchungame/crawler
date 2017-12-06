package com.xlh.crawler.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TesserocrUtil {

    private static ITesseract instance = new Tesseract();

    synchronized public static int imageRecog(HttpClient client) throws IOException, TesseractException {
        HttpGet httpGet=new HttpGet("http://cpquery.sipo.gov.cn/freeze.main?txn-code=createImgServlet&freshStept=1");
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        String fileName=String.valueOf((new Date()).getTime());
        File file = new File("d:/logs/"+fileName+".jpg");
//        File file = new File("/home/zc/tmpfile/"+fileName+".jpg");
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
        instance.setDatapath("C:/Program Files (x86)/Tesseract-OCR/tessdata");
//        instance.setDatapath("/usr/share/tesseract-ocr/tessdata");
        String str = instance.doOCR(file);
        System.out.println("文件："+fileName+"识别结果："+str);
        String pattern="(\\d)(\\+|\\-)(\\d)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        if (m.find()) {
            int first = new Integer(m.group(1));
            int sec = new Integer(m.group(3));
            int result;
            if (m.group(2).equals("+")) {
                result = first + sec;
            } else {
                result = first - sec;
            }
            return result;
        }else {
            return imageRecog(client);
        }
    }
}
