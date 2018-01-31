#coding:UTF-8

import requests
from bs4 import BeautifulSoup
import threading,time,sys
import pymysql
from DBUtils.PooledDB import PooledDB

sys.path.append('../')
import configUtil,proxyUtil
#

pool = PooledDB(pymysql,5,host='172.16.11.135',user='root',passwd='root',db='xlh_crawler',port=3306,charset='utf8',cursorclass=pymysql.cursors.DictCursor) #5为连接池里的最少连接数

def test(session,info):

    # url="http://www.qichacha.com/"
    # resp=session.get(url)
    #
    # resp=session.get("http://ip.chinaz.com/getip.aspx")
    # text=resp.text
    # print proxy+"==="+text
    url="http://www.qichacha.com/search?key="+info['enterprise_name'].encode("UTF-8")
    print url
    resp=session.get(url)
    if resp.status_code==200:
        if resp.text.find("<script>window.location.href")!=-1 :
            raise Exception("被屏蔽了"+info['enterprise_name'])
        soap=BeautifulSoup( resp.text,"html.parser")
        countOld=soap.find(id="countOld")
        if countOld is None:
            raise Exception("被屏蔽了countOld"+info['enterprise_name'].encode("UTF-8"))

        sql="update cdm_ent_dto_corp_info set status=1 where id="+str(info['id'])
        cursor.execute(sql)
        conn.commit()
        linksDoc=soap.find_all(class_="ma_h1")
        if len(linksDoc) <1:
            print "ssssssssssssssssssssss  len(linksDoc) <1"
        for link in linksDoc:
            print link['href']
            sql="insert into qcc_company_urls (qichacha_url,insert_time) values('"+link['href']+"',"+str(int(time.time()))+")"
            cursor.execute(sql)
            conn.commit()
    else:
        raise Exception("被屏蔽了 resp.status_code="+str(resp.status_code)+info['enterprise_name'].encode("UTF-8"))

conn = pool.connection()  #以后每次需要数据库连接就是用connection（）函数获取连接就好了
cursor=conn.cursor()


# semaphore=threading.Semaphore(1)
times=0;
session=requests.session()
# proxy=proxyUtil.outputProxy()
# session.proxies={ "http": "socks5://127.0.0.1:1080", "https": "socks5://127.0.0.1:1080", }
headers={'Host': 'www.qichacha.com',
         'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:58.0) Gecko/20100101 Firefox/58.0',
         'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
         'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
         'Accept-Encoding': 'gzip, deflate',
         'Connection': 'keep-alive',
         # 'Cookie': 'acw_tc=AQAAAMgeuTUQtwUA4pFAcII+2u9T1xTP; acw_sc__=5a715ac34d6a245b8d0d5d4c96c6b2b2cef64367; PHPSESSID=1894os2ie3fh8ksinn4pi7lag2; zg_did=%7B%22did%22%3A%20%221614aca9436b3c-0bbaed1712d8f7-3a75045d-1fa400-1614aca9437b74%22%7D; UM_distinctid=1614aca9483a46-01b5eb645fb575-3a75045d-1fa400-1614aca9484bc5; CNZZDATA1254842228=2040120175-1517374401-null%7C1517374401; hasShow=1; zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f=%7B%22sid%22%3A%201517378245691%2C%22updated%22%3A%201517378253915%2C%22info%22%3A%201517378245693%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22www.qichacha.com%22%7D',
         'Referer':'http://www.qichacha.com/',
         'Upgrade-Insecure-Requests': '1'}
session.headers=headers
# resp=session.get("http://www.qichacha.com/")

sql="select * from qcc_company_urls limit 0,1000"
cursor.execute(sql)
cds=cursor.fetchall()
for cd in cds:
    url="http://www.qichacha.com"+cd['qichacha_url']
    resp=session.get(url)

print resp.text
# while 1:
#     sql="select * from  cdm_ent_dto_corp_info where status=0 limit 0,50"
#     cursor.execute(sql)
#     cds=cursor.fetchall()
#     for cd in cds:
#         # proxy=proxyUtil.outputProxy()
#         # session=requests.session()
#         session.max_redirects=1
#         session.timeout=1
#         session.proxies={ "http": "socks5://127.0.0.1:1080", "https": "socks5://127.0.0.1:1080", }
#         try:
#             # print 'startR watchThreads len='+str(len(watchThreads))
#             # semaphore.acquire()
#             test(session,cd)
#         except Exception,e:
#             print "exception-",e.message
#         # print times
#         # times=times+1


