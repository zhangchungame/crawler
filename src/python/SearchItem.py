#coding:UTF-8
import requests
import PyV8
import json
import time

def getGTChallenge(session):
    loginurl="http://www.gsxt.gov.cn/SearchItemCaptcha"
    result=session.get(loginurl)
    mycookies= result.cookies
    resp=result.text
    resp=resp[8:]
    tmp=resp.split('</script')
    resp=tmp[0]
    resp=resp.replace("eval(y.replace", "var aaa=(y.replace");
    resp = resp + "aaa=aaa.replace(\"while(window._phantom||window.__phantomas){};\", \"\");bbb = aaa.split(\"setTimeout\");aaa = bbb[0] + \"return dc;}\";aaa = aaa.replace(\"var l=\", \"\");";
    ctxt = PyV8.JSContext()
    ctxt.enter()
    func = ctxt.eval(resp)
    fu=ctxt.eval("("+func+")")
    jslarr= fu().split("=")
    jsl_clearance=jslarr[1]
    session.cookies['__jsl_clearance']=jsl_clearance
    result=session.get(loginurl)
    print result.text
    resp=json.loads(result.text)
    return  resp

def getImageGif(session):
    url="http://www.gsxt.gov.cn/corp-query-custom-geetest-image.gif?v="
    localTime=time.localtime(time.time())
    url=url+(localTime.tm_min+localTime.tm_sec)
    session.get(url)

session=requests.session()
headers={'Host': 'www.gsxt.gov.cn',
         'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0',
         'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
         'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
         'Accept-Encoding': 'gzip, deflate',
         'Referer': 'http://www.gsxt.gov.cn/SearchItemCaptcha',
         'Connection': 'keep-alive',
         'Upgrade-Insecure-Requests': '1',
         'Cache-Control': 'max-age=0, no-cache'}
session.headers=headers




