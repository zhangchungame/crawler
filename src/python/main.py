#coding:UTF-8
import proxyUtil
import SearchItem,commonUtil
import threading,time
import pymysql
import configUtil
from DBUtils.PooledDB import PooledDB
#
conn = configUtil.pool.connection()  #以后每次需要数据库连接就是用connection（）函数获取连接就好了
cursor=conn.cursor()
sql="select enterprise_name from  cdm_ent_dto_corp_info limit 0,50"
cursor.execute(sql)
cds=cursor.fetchall()
print cds
# t=threading.Thread(target=proxyUtil.startR)
# t.start()
# cds=[["来伊份"],["信隆行"],["尚社生活"]]
semaphore=threading.Semaphore(5)
# t1=SearchItem.SearchItem("来伊份",None,semaphore)
# t1.start()

watchThreads=[]
t=commonUtil.ThreadTimeOut(watchThreads)
t.start()
while 1:
    for cd in cds:
        try:
            # print 'startR watchThreads len='+str(len(watchThreads))
            semaphore.acquire()
            proxy=proxyUtil.outputProxy()
            print cd['enterprise_name'],proxy
            t1=SearchItem.SearchItem(cd['enterprise_name'],proxy,semaphore)
            t1.start()
            watchThreads.append(t)

        except Exception, e:
            print 'main e.message:\t', e.message
    time.sleep(1)
print "finish aaaaaaaaaaaaaaaaaaaaaa"



