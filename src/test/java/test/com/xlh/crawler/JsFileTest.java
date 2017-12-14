package test.com.xlh.crawler;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsFileTest {
    @Test
    public void test() throws IOException {
        File file=new File("./geetest.6.0.5.js");
        InputStream io=new FileInputStream(file);
        BufferedReader reader=new BufferedReader(new InputStreamReader(io));
        StringBuilder sb=new StringBuilder();
        String line=null;
        try {

            while ((line = reader.readLine()) != null) {

                sb.append(line);

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                io.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
        String aaa=sb.toString();
        String pattern="(\\\\x[0-9a-f]{1,4})+";
        Pattern r = Pattern.compile(pattern);

//        aaa="\\x69\\x6e\\x70\\x75\\x74\\x2e\\x67\\x65\\x65\\x74\\x65\\x73\\x74\\x5f\\x63\\x68\\x61\\x6c\\x6c\\x65\\x6e\\x67\\x65";
        // 现在创建 matcher 对象
        List<Map<String,String>> list=new ArrayList<>();
        Matcher m = r.matcher(aaa);
        while(m.find()){
            System.out.println((m.group(0)));
            System.out.println(hexString2String(m.group(0).replace("\\x","")));
            Map map=new HashMap();
            map.put("old",m.group(0));
            map.put("new",hexString2String(m.group(0).replace("\\x","")));
            list.add(map);
        }
        for(int i=0;i<list.size();i++){
            aaa=aaa.replace(list.get(i).get("old"),list.get(i).get("new"));
        }
        System.out.println(aaa);
        File file1=new File("./bb.js");


        FileWriter fw = new FileWriter(file1);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(aaa);
        bw.close();
    }

    public static String string2HexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }
    public static String hexString2String(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp
                    + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2),
                    16).byteValue();
        }
        return temp;
    }
}
