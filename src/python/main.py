#coding:UTF-8
import proxyUtil
import SearchItem
import threading
import pymysql
from DBUtils.PooledDB import PooledDB
pool = PooledDB(pymysql,5,host='172.16.11.135',user='root',passwd='root',db='xlh_crawler',port=3306,charset='utf8') #5为连接池里的最少连接数

conn = pool.connection()  #以后每次需要数据库连接就是用connection（）函数获取连接就好了
cursor=conn.cursor()
sql="select enterprise_name from  cdm_ent_dto_corp_info limit 0,50"
cursor.execute(sql)
cds=cursor.fetchall()
print cds
semaphore=threading.Semaphore(1)
for cd in cds:
    try:
        semaphore.acquire()
        proxy=proxyUtil.outputProxy()
        print cd[0],proxy
        t1=SearchItem.SearchItem(cd[0],proxy,semaphore)
        t1.start()
    except Exception, e:
        print 'e.message:\t', e.message


# while 1:
#     try:
#         proxy=proxyUtil.outputProxy()
#         if SearchItem.getCorpUrl("来伊份",proxy)==1:
#             break
#     except Exception, e:
#         print 'e.message:\t', e.message
print "finish aaaaaaaaaaaaaaaaaaaaaa"