#coding:UTF-8
import requests
import PyV8
import json
import time
import re

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
    challengeJson=json.loads(result.text)
    return  challengeJson

def getImageGif(session):
    url="http://www.gsxt.gov.cn/corp-query-custom-geetest-image.gif?v="
    localTime=time.localtime(time.time())
    url=url+str(localTime.tm_min+localTime.tm_sec)
    resp=session.get(url)
    ctxt = PyV8.JSContext()
    ctxt.enter()
    funstr="""(function(){var json="""+resp.text+""";return json.map( function(item){ return String.fromCharCode(item);}).join("");})"""
    func = ctxt.eval(funstr)
    aaa=func()
    matchObj = re.search( 'location_info = (\d+);', aaa)
    if matchObj:
        return matchObj.group(1)
    else:
        Exception("没有找到location_info")

def getValidateInput(session,location_info):
    url="http://www.gsxt.gov.cn/corp-query-geetest-validate-input.html?token="+location_info
    resp=session.get(url)
    ctxt = PyV8.JSContext()
    ctxt.enter()
    funstr="""(function(){var json="""+resp.text+""";return json.map( function(item){ return String.fromCharCode(item);}).join("");})"""
    func = ctxt.eval(funstr)
    aaa=func()
    matchObj = re.search( 'value: (\d+)}', aaa)
    if matchObj:
        location_info= matchObj.group(1)
        token=int(location_info) ^ 536870911;
        print "token=",token
        return str(token)
    else:
        Exception("没有找到location_info")

def searchTest(session,keyword):
    url="http://www.gsxt.gov.cn/corp-query-search-test.html?searchword="+keyword
    resp=session.get(url);
    print "searchTest ",resp.text

def jianYan(session,challengeJson):
    url="http://jiyanapi.c2567.com/shibie?user=dandinglong&pass=aaa222&gt="+challengeJson["gt"]+"&challenge="+challengeJson["challenge"]+"&referer=http://www.gsxt.gov.cn&return=json&format=utf8"
    resp=session.get(url);
    jiyanJson=  json.loads(resp.text)
    return jiyanJson

def querySearch(session,jiYanJson,token,keyword):
    url="http://www.gsxt.gov.cn/corp-query-search-1.html"
    postData={
        'tab':'ent_tab',
        'province':'',
        'geetest_challenge':jiYanJson['challenge'],
        'geetest_validate':jiYanJson['validate'],
        'geetest_seccode':jiYanJson['validate']+'|jordan',
        'token':token,
        'searchword':keyword
    }
    resp=session.post(url,postData)
    print resp.text


def getCorpUrl(keyword):
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
    challengeJson=getGTChallenge(session)
    jiyanJson=jianYan(session,challengeJson)
    localtion_info= getImageGif(session)
    token=getValidateInput(session,localtion_info)
    searchTest(session,keyword)
    return querySearch(session,jiyanJson,token,keyword)

getCorpUrl("启信宝")




