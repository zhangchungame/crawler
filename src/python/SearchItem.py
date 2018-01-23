#coding:UTF-8
import requests
import PyV8
def getcookie():
    session=requests.session()
    cookies={
        '__jsluid':'d5e11efb02ec4ec01f2588e5402ec21e',
        '__jsl_clearance':'1516634254.793|0|ncLzQttid1W97lQ%2FS7VyCTdm0LY%3D'
    }
    headers={'Host': 'www.gsxt.gov.cn',
                     'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0',
                     'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
                     'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
                     'Accept-Encoding': 'gzip, deflate',
                     'Referer': 'http://www.gsxt.gov.cn/SearchItemCaptcha',
                     'Connection': 'keep-alive',
                     'Upgrade-Insecure-Requests': '1',
                     # 'Cookie': '__jsluid=e2fcecd7d3d3ff93d9f464a2f97e32e4; __jsl_clearance=1516615977.739|0|tc41z8ajXiSTwRNW3RTvRHMfW9M%3D',
                     'Cache-Control': 'max-age=0, no-cache'}
    loginurl="http://www.gsxt.gov.cn/SearchItemCaptcha"
    result=requests.get(loginurl,headers=headers,cookies=cookies)
    print result.text
    cookies=result.cookies
    print cookies


headers={'Host': 'www.gsxt.gov.cn',
         'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0',
         'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
         'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
         'Accept-Encoding': 'gzip, deflate',
         'Referer': 'http://www.gsxt.gov.cn/SearchItemCaptcha',
         'Connection': 'keep-alive',
         'Upgrade-Insecure-Requests': '1',
         # 'Cookie': '__jsluid=e2fcecd7d3d3ff93d9f464a2f97e32e4; __jsl_clearance=1516615977.739|0|tc41z8ajXiSTwRNW3RTvRHMfW9M%3D',
         'Cache-Control': 'max-age=0, no-cache'}
loginurl="http://www.gsxt.gov.cn/SearchItemCaptcha"
result=requests.get(loginurl,headers=headers)
mycookies= result.cookies
resp=result.text
resp=resp[8:]
tmp=resp.split('</script')
resp=tmp[0]
resp=resp.replace("eval(y.replace", "var aaa=(y.replace");
resp = resp + "aaa=aaa.replace(\"while(window._phantom||window.__phantomas){};\", \"\");bbb = aaa.split(\"setTimeout\");aaa = bbb[0] + \"return dc;}\";aaa = aaa.replace(\"var l=\", \"\");";

# getcookie()
ctxt = PyV8.JSContext()
ctxt.enter()
func = ctxt.eval(resp)
fu=ctxt.eval("("+func+")")
jslarr= fu().split("=")
jsl_clearance=jslarr[1]

mycookies['__jsl_clearance']=jsl_clearance
result=requests.get(loginurl,headers=headers,cookies=mycookies)




print result.cookies