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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

public class SearchItemTest {

    @Test
    public void test() throws IOException, ScriptException {
        String resp="<script>var x=\"fromCharCode@window@replace@id1W9@0@if@17@dc@Expires@cd@document@false@34@22@1516634254@attachEvent@i@captcha@__phantomas@16@addEventListener@href@charAt@catch@2@Mon@GMT@__jsl_clearance@x@1@eval@f@try@18@location@String@for@challenge@l@CTd@8@else@@t@join@3@cookie@return@793@Path@1500@F@DOMContentLoaded@function@length@var@onreadystatechange@setTimeout@z@4@Jan@e@while@_phantom\".replace(/@*$/,\"\").split(\"@\"),y=\"1k 13=1i(){1r(2.1s||2.j){};1k a,8='s=f.1d|5|';1k w=[1i(t){1c t},1i(t){1c t;},1i(t){1c v('10.1('+t+')')}];a=[((-~![]<<-~![])+[]+[])+(-~![]+[[]][~~[]]),(-~[]+(-~{}+[(+[])])/[-~-~{}]+[]),(-~![]+[[]][~~[]])+(-~![]+[[]][~~[]]),(-~![]+[[]][~~[]])+((-~[]<<-~~~[]-~[]-~[])+[]+[]),((-~![]<<-~![])+(-~![]+[(-~![]<<-~![])]>>(-~![]<<-~![]))+[-~{}, []][-~![]]),((-~[]<<-~~~[]-~[]-~[])+[]+[]),((-~![]<<-~![])+[]+[]),[(+[])],[((+!!{})|p)],(1a+(-~![]<<-~-~{})+[]+[[]][~~[]]),(-~![]+[[]][~~[]]),[1o],((-~![]<<-~![])+[]+[])+[(+[])],(-~![]+[[]][~~[]])+[1o],(-~![]+[[]][~~[]])+(-~(15)+[]),(-~![]+[[]][~~[]])+(-~[]+(-~{}+[(+[])])/[-~-~{}]+[]),(-~![]+[[]][~~[]])+(1a+(-~![]<<-~-~{})+[]+[[]][~~[]]),(-~(15)+[]),(-~![]+[[]][~~[]])+((-~![]<<-~![])+[]+[]),(-~![]+[[]][~~[]])+((-~![]<<-~![])+(-~![]+[(-~![]<<-~![])]>>(-~![]<<-~![]))+[-~{}, []][-~![]]),(-~![]+[[]][~~[]])+[((+!!{})|p)],(-~![]+[[]][~~[]])+[(+[])]];11(1k h=5;h<a.1j;h++){a[h]=w[[5,p,u,p,u,5,u,5,u,p,5,u,p,5,p,5,u,p,u,p,5,p][h]]([(1a+(-~![]<<-~-~{})+[]+[[]][~~[]]),((-~![]<<-~![])+[]+[]),'4','13','1g','18',[(1a+(-~![]<<-~-~{})+[]+[[]][~~[]])+(-~[]+(-~{}+[(+[])])/[-~-~{}]+[])],[((-~[]<<-~~~[]-~[]-~[])+[]+[])+(-~![]+[[]][~~[]]),[((+!!{})|p)]+(1a+(-~![]<<-~-~{})+[]+[[]][~~[]])],(!!{}+[[]][~~[]]).n(~~!{}),[(-~![]+[[]][~~[]])+[(+[])]+(-~(15)+[])],[(-~[]+(-~{}+[(+[])])/[-~-~{}]+[])+((-~[]<<-~~~[]-~[]-~[])+[]+[])],'1n','5',[((+!!{})|p)],(1a+(-~![]<<-~-~{})+[]+[[]][~~[]]),[(1a+(-~![]<<-~-~{})+[]+[[]][~~[]])+(-~[]+(-~{}+[(+[])])/[-~-~{}]+[]),((-~[]<<-~~~[]-~[]-~[])+[]+[])+(-~(15)+[]),[((+!!{})|p)]+(1a+(-~![]<<-~-~{})+[]+[[]][~~[]])],[(-~~~[]-~[]-~[])/~~!{}+[[]][~~[]]][5].n(-~-~{}-~~~[]+(-~![]<<-~-~{})),'14',[((-~[]<<-~~~[]-~[]-~[])+[]+[])+(-~![]+[[]][~~[]])],[((-~[]<<-~~~[]-~[]-~[])+[]+[])+(-~[]+(-~{}+[(+[])])/[-~-~{}]+[])],[((-~[]<<-~~~[]-~[]-~[])+[]+[])+[((+!!{})|p)]],((-~~~[]-~[]-~[])/~~!{}+[]).n((-~![]<<-~-~{}))+({}+[]+[[]][~~[]]).n((-~![]+[[]][~~[]])+((-~![]<<-~![])+[]+[]))][a[h]])};a=a.19('');8+=a;1m('z.m=z.m.3(/[\\\\?|&]i-12/,\\\\'\\\\')',1f);b.1b=(8+';9=q, e-1p-y k:7:d r;1e=/;');};6((1i(){x{1c !!2.l;}o(1q){1c c;}})()){b.l('1h',13,c);}16{b.g('1l',13);}\",z=0,f=function(x,y){var a=0,b=0,c=0;x=x.split(\"\");y=y||99;while((a=x.shift())&&(b=a.charCodeAt(0)-77.5))c=(Math.abs(b)<13?(b+48.5):parseInt(a,36))+y*c;return c},g=y.match(/\\b\\w+\\b/g).sort(function(x,y){return f(x)-f(y)}).pop();while(f(g,++z)-x.length){};eval(y.replace(/\\b\\w+\\b/g, function(y){return x[f(y,z)-1]}));</script>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ";
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
    }
}
