#coding:UTF-8
import requests
def getcookie():
    session=requests.session()
    session.cookies={
        '__jsluid':'e2fcecd7d3d3ff93d9f464a2f97e32e4',
        '__jsl_clearance':'1516615977.739|0|tc41z8ajXiSTwRNW3RTvRHMfW9M%3D'
    }
    session.headers={'Host': 'www.gsxt.gov.cn',
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
    result=session.get(loginurl)
    print result.text
    cookies=result.cookies
    print cookies

getcookie()