#coding:UTF-8
import json
import time
import requests
import PyV8
import re
from bs4 import BeautifulSoup

headers={'Host': 'www.gsxt.gov.cn',
         'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0',
         'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
         'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
         'Accept-Encoding': 'gzip, deflate',
         'Referer': 'http://www.gsxt.gov.cn/SearchItemCaptcha',
         'Connection': 'keep-alive',
         'Upgrade-Insecure-Requests': '1',
         'Cache-Control': 'max-age=0, no-cache'}
session=requests.session()
session.headers=headers
url="http://www.gsxt.gov.cn/%7BeZQclOkVx-kJo-uUqBbPcvG3Z0brxXv2YkomJ00QIsZZXJa8M4GE_JI8Zpd-dyiMx5fAcMld-ELEWzWxjYUfr8RHf85HhQy1j30xqbI7o8ynPtEl_hamjdY1-puVBUT-rEjG4Q_4vjNF6PLYF5TbXm9MEIoYc5YHChcP6qG76Ng-1516931232774%7D"
resp=session.get(url)
mycookies= resp.cookies
resp=resp.text
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
resp=session.get(url)
html= resp.text
del mycookies,fu,func,headers

corpInfo={}
soup=BeautifulSoup(html)
#营业执照信息
divs=soup.find_all(class_="overview")
div=divs[0]
dls=div.find_all("dl")
corpInfo['col1_1']= dls[0].dt.contents[0].strip()
corpInfo['col1_2']= dls[0].dd.contents[1].strip()
corpInfo['col1_3']= dls[1].dd.contents[0].strip()
corpInfo['col1_4']= dls[2].dd.contents[0].strip()
corpInfo['col1_5']= dls[3].dd.contents[0].strip()
corpInfo['col1_6']= dls[4].dd.contents[0].strip()
corpInfo['col1_7']= dls[5].dd.contents[0].strip()
corpInfo['col1_8']= dls[6].dd.contents[0].strip()
corpInfo['col1_9']= dls[7].dd.contents[0].strip()
corpInfo['col1_10']= dls[8].dd.contents[0].strip()
corpInfo['col1_11']= dls[9].dd.contents[0].strip()
corpInfo['col1_12']= dls[10].dd.contents[0].strip()
corpInfo['col1_13']= dls[11].dd.contents[0].strip()
corpInfo['col1_14']= dls[12].dd.contents[0].strip()
del divs,div,dls

#股东及出资信息
corpInfo['col2_1']=[]
urldiv=soup.find(id="url")
urlsStr=urldiv.script.contents[0]
matchObj = re.search( 'var shareholderUrl = "(.*)";', urlsStr)
if matchObj is None:
    Exception("没有股东出资信息url")
url="http://www.gsxt.gov.cn"+matchObj.group(1)
postData={
    'draw':1,
    'start':0,
    'length':5,
}
resp=session.post(url,postData)
gudongJson=json.loads(resp.text)
corpInfo['col2_2']=gudongJson['recordsTotal']
for val in gudongJson['data']:
    corpInfo['col2_1'].append(val)
while 1:
    try:
        time.sleep(3)
        if postData['draw']<gudongJson['totalPage']:
            postData['start']=gudongJson['draw']*gudongJson['perPage']
            postData['draw']=gudongJson['draw']+1
            resp=session.post(url,postData)
            gudongJson=json.loads(resp.text)
            for val in gudongJson['data']:
                corpInfo['col2_1'].append(val)
        else:
            break
    except Exception,e:
        print resp.text
        print e.message
del  gudongJson

#主要人员信息
corpInfo['col3_1']=[]
matchObj = re.search( 'var keyPersonUrl = "(.*)";', urlsStr)
if matchObj is None:
    Exception("没有主要人员信息url")
url="http://www.gsxt.gov.cn"+matchObj.group(1)
resp=session.post(url,postData)
mainPersonJson=json.loads(resp.text)
corpInfo['col3_2']=mainPersonJson['recordsTotal']
for val in mainPersonJson['data']:
    corpInfo['col3_1'].append(val)
while 1:
    try:
        time.sleep(3)
        if postData['draw']<mainPersonJson['totalPage']:
            postData['start']=mainPersonJson['draw']*mainPersonJson['perPage']
            postData['draw']=mainPersonJson['draw']+1
            resp=session.post(url,postData)
            mainPersonJson=json.loads(resp.text)
            for val in mainPersonJson['data']:
                corpInfo['col3_1'].append(val)
        else:
            break
    except Exception,e:
        print resp.text
        print e.message
del  mainPersonJson

#分支机构信息
corpInfo['col4_1']=[]
matchObj = re.search( 'var branchUrl = "(.*)";', urlsStr)
if matchObj is None:
    Exception("没有分支机构信息url")
url="http://www.gsxt.gov.cn"+matchObj.group(1)
resp=session.post(url,postData)
branchJson=json.loads(resp.text)
corpInfo['col4_2']=branchJson['recordsTotal']
for val in branchJson['data']:
    corpInfo['col4_1'].append(val)
while 1:
    try:
        time.sleep(3)
        if postData['draw']<branchJson['totalPage']:
            postData['start']=branchJson['draw']*branchJson['perPage']
            postData['draw']=branchJson['draw']+1
            resp=session.post(url,postData)
            branchJson=json.loads(resp.text)
            for val in branchJson['data']:
                corpInfo['col4_1'].append(val)
        else:
            break
    except Exception,e:
        print resp.text
        print e.message
del  branchJson


#变更信息
corpInfo['col5_1']=[]
matchObj = re.search( 'var alterInfoUrl = "(.*)";', urlsStr)
if matchObj is None:
    Exception("没有分支机构信息url")
url="http://www.gsxt.gov.cn"+matchObj.group(1)
resp=session.post(url,postData)
branchJson=json.loads(resp.text)
corpInfo['col5_2']=branchJson['recordsTotal']
for val in branchJson['data']:
    corpInfo['col5_1'].append(val)
while 1:
    try:
        time.sleep(3)
        if postData['draw']<branchJson['totalPage']:
            postData['start']=branchJson['draw']*branchJson['perPage']
            postData['draw']=branchJson['draw']+1
            resp=session.post(url,postData)
            branchJson=json.loads(resp.text)
            for val in branchJson['data']:
                corpInfo['col5_1'].append(val)
        else:
            break
    except Exception,e:
        print resp.text
        print e.message
del  branchJson
for val in corpInfo:
    print val+"====",corpInfo[val]