# coding:UTF-8

import requests

session = requests.session();
head = {
    'Host': 'wenshu.court.gov.cn',
    'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:58.0) Gecko/20100101 Firefox/58.0',
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
    'Accept-Encoding': 'gzip, deflate',
    'Connection': 'keep-alive',
    'Upgrade-Insecure-Requests': '1',
    'Cache-Control': 'max-age=0',
}

session.headers = head

postData = {
    'Param': '案件类型:执行案件',
    'Index': '4',
    'Page': '5',
    'Order': '法院层级',
    'Direction': 'asc',
    'vl5x': '3d4f359f41e8fba4229743ad',
    'number': 'L9NZMBW3&guid=9521d70b-1b2d-17aff056-b7cd58b6cff9'
}
resp = session.get("http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+5+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E6%89%A7%E8%A1%8C%E6%A1%88%E4%BB%B6")
print resp.text
resp = session.post("http://wenshu.court.gov.cn/List/ListContent", postData)
print resp.text
