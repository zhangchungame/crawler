#coding:UTF-8
import requests
import jpype
from jpype import *
import json
import time
import re
import redis
import threading
from bs4 import BeautifulSoup
import configUtil
import PyV8



class SearchItem(threading.Thread):
    redisPool = configUtil.redisPool
    session=requests.session()
    keyword=""
    proxy=""
    semaphore=None
    def getGTChallenge(self):
        print "getGTChallenge start"
        loginurl="http://www.gsxt.gov.cn/SearchItemCaptcha"
        result=self.session.get(loginurl)
        if "y.replace(" not in result.text:
            raise Exception("被屏蔽了")
        mycookies= result.cookies
        jpype.attachThreadToJVM()
        jpype.isThreadAttachedToJVM()
        A = jpype.JClass("com.GovTest")
        self.Aobj=A()
        fu=self.Aobj.challenge(result.text)
        # resp=resp[8:]
        # tmp=resp.split('</script')
        # resp=tmp[0]
        # resp=resp.replace("eval(y.replace", "var aaa=(y.replace");
        # resp = resp + "aaa=aaa.replace(\"while(window._phantom||window.__phantomas){};\", \"\");bbb = aaa.split(\"setTimeout\");aaa = bbb[0] + \"return dc;}\";aaa = aaa.replace(\"var l=\", \"\");";
        # ctxt = PyV8.JSContext()
        # ctxt.enter()
        # func = ctxt.eval(resp)
        # fu=ctxt.eval("("+func+")")
        print "fu="+fu
        jslarr= fu.split("=")
        jsl_clearance=jslarr[1]
        self.session.cookies['__jsl_clearance']=jsl_clearance
        result=self.session.get(loginurl)
        challengeJson=json.loads(result.text)
        return  challengeJson

    def getImageGif(self):
        print "getImageGif start"
        url="http://www.gsxt.gov.cn/corp-query-custom-geetest-image.gif?v="
        localTime=time.localtime(time.time())
        url=url+str(localTime.tm_min+localTime.tm_sec)
        resp=self.session.get(url)
        # ctxt = PyV8.JSContext()
        # ctxt.enter()
        # funstr="""(function(){var json="""+resp.text+""";return json.map( function(item){ return String.fromCharCode(item);}).join("");})"""
        # func = ctxt.eval(funstr)
        # aaa=func()
        aaa=self.Aobj.getImageGif(resp.text)
        matchObj = re.search( 'location_info = (\d+);', aaa)
        if matchObj:
            return matchObj.group(1)
        else:
            Exception("没有找到location_info")

    def getValidateInput(self,location_info):
        print "getValidateInput start"
        url="http://www.gsxt.gov.cn/corp-query-geetest-validate-input.html?token="+location_info
        resp=self.session.get(url)
        # ctxt = PyV8.JSContext()
        # ctxt.enter()
        # funstr="""(function(){var json="""+resp.text+""";return json.map( function(item){ return String.fromCharCode(item);}).join("");})"""
        # func = ctxt.eval(funstr)
        # aaa=func()
        aaa=self.Aobj.getImageGif(resp.text)
        matchObj = re.search( 'value: (\d+)}', aaa)
        if matchObj:
            location_info= matchObj.group(1)
            token=int(location_info) ^ 536870911;
            print "token=",token
            return str(token)
        else:
            Exception("没有找到location_info")

    def searchTest(self,keyword):
        print "searchTest start"
        url="http://www.gsxt.gov.cn/corp-query-search-test.html?searchword="+keyword
        resp=self.session.get(url);
        print "searchTest ",resp.text

    def jianYan(self,challengeJson):
        print "jianYan start"
        url="http://jiyanapi.c2567.com/shibie?user=dandinglong&pass=aaa222&gt="+challengeJson["gt"]+"&challenge="+challengeJson["challenge"]+"&referer=http://www.gsxt.gov.cn&return=json&format=utf8"
        sess=requests.session()
        resp=sess.get(url);
        jiyanJson=  json.loads(resp.text)
        print resp.text
        return jiyanJson

    def querySearch(self,jiYanJson,token,keyword):
        print "querySearch start"
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
        resp=self.session.post(url,postData)
        return resp.text ,postData

    def dealPageUrl(self,html):
        print "dealPageUrl start"
        soup = BeautifulSoup(html,"html.parser")
        urlsItem=soup.find_all("a",class_="search_list_item db")
        pageNums=0
        r = redis.Redis(connection_pool=self.redisPool)
        for urlItem in urlsItem:
            print "urlItem['href']=",urlItem['href']
            r.rpush("corpUrlList",urlItem['href'])
        if len(urlsItem)>1:
            pageForm=soup.find_all(id="pageForm")
            tabAs=pageForm[0].find_all("a",text=re.compile("\d+"))
            pageNums=len(tabAs)
        return pageNums

    def dealPageUrlNum(self,pageNums,postData):
        print "dealPageUrlNum start"
        url="http://www.gsxt.gov.cn/corp-query-search-advancetest.html"
        r = redis.Redis(connection_pool=self.redisPool)
        for i in range(pageNums):
            postData['page']=i+1
            resp=self.session.get(url,params=postData)
            soup = BeautifulSoup(resp.text)
            urlsItem=soup.find_all("a",class_="search_list_item db")
            for urlItem in urlsItem:
                print "urlItem['href']=",urlItem['href']
                r.rpush("corpUrlList",urlItem['href'])


    def getCorpUrl(self):
        self.session.timeout=1
        self.session.max_redirects=1
        if self.proxy:
            self.session.proxies={ "http": "http://"+self.proxy, "https": "http://"+self.proxy, }
        headers={'Host': 'www.gsxt.gov.cn',
                 'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0',
                 'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
                 'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
                 'Accept-Encoding': 'gzip, deflate',
                 'Referer': 'http://www.gsxt.gov.cn/SearchItemCaptcha',
                 'Connection': 'keep-alive',
                 'Upgrade-Insecure-Requests': '1',
                 'Cache-Control': 'max-age=0, no-cache'}
        self.session.headers=headers
        challengeJson=self.getGTChallenge()
        localtion_info= self.getImageGif()
        token=self.getValidateInput(localtion_info)
        self.searchTest(self.keyword)
        jiyanJson=self.jianYan(challengeJson)
        html,postData=self.querySearch(jiyanJson,token,self.keyword)
        pageNums=self.dealPageUrl(html)
        print 'pageNums=',pageNums
        self.dealPageUrlNum(pageNums,postData)
        return 1

    def run(self):  # 定义每个线程要运行的函数
        try:
            self.getCorpUrl()
        except Exception,e:
            print "run exception ",e.message
        self.session.close()
        self.semaphore.release()
        print "search Item run finish"

    def __init__(self, keyword,proxy,semaphore):
        threading.Thread.__init__(self)
        self.keyword = keyword
        self.proxy = proxy
        self.semaphore = semaphore









