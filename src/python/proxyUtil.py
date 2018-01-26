#coding:UTF-8

import redis
import requests
import threading
import time
import commonUtil
import socket
socket.setdefaulttimeout(1)

redisPool = redis.ConnectionPool(host='172.16.11.70', port=6379, db=8, password='redis')

class proxyCheckInsert(threading.Thread):
    timeoutSec=5
    startReqTime=time.time()
    session=requests.session()
    def dealTimeOut(self):
        if time.time()-self.startReqTime>self.timeoutSec:
            pass
            # self.session.close()
            print 'proxyCheckInsert dealTimeOut session close'+str(self.threadNum)
    def run(self):  # 定义每个线程要运行的函数
        try:
            if self.check():
                self.insert()
        except Exception,e:
            print e.message
        self.semaphore.release()
        print 'proxyCheckInsert run finish threadNum='+str(self.threadNum)

    def __init__(self, proxy,semaphore,threadNum):
        threading.Thread.__init__(self)
        self.proxy = proxy
        self.semaphore = semaphore
        self.threadNum = threadNum
    def check(self):
        print "check start"+str(self.threadNum),self.proxy
        self.session.proxies={ "http": "http://"+self.proxy, "https": "http://"+self.proxy, }
        self.session.timeout=1
        self.session.max_redirects=1
        try:
            self.startReqTime=time.time()
            resp=self.session.get("http://ip.chinaz.com/getip.aspx")
            # print "second req start "+str(self.threadNum)
            # self.startReqTime=time.time()
            # resp=self.session.get("http://ip.chinaz.com/getip.aspx")
            text=resp.text
            # print self.proxy+"==="+text
            if text.find("address")==-1:
                raise Exception("no address",self.proxy)
            return self.proxy
        except Exception,e:
            print "check message "+str(self.threadNum),e.message
            return ""
    def insert(self):
        print "insert "+str(self.threadNum)
        r = redis.Redis(connection_pool=redisPool)
        r.rpush("daxiang_proxy",self.proxy)



def getProxy():
    url="http://tvp.daxiangdaili.com/ip/?tid=557552170840411&num=5&delay=3&category=2&sortby=time&filter=on"
    session=requests.session()
    resp=session.get(url)
    tmp=resp.text.split("\r\n")
    return tmp


def startR():
    watchThreads=[]
    r=redis.Redis(connection_pool=redisPool)
    t=commonUtil.ThreadTimeOut(watchThreads)
    t.start()
    semaphore=threading.Semaphore(1)
    threadNum=0;
    while 1:
        print 'startR watchThreads len='+str(len(watchThreads))
        if(r.llen("daxiang_proxy")<15):
            proxys=getProxy()
            for proxy in proxys:
                semaphore.acquire()
                t=proxyCheckInsert(proxy,semaphore,threadNum)
                t.start()
                threadNum=threadNum+1
                watchThreads.append(t)
        time.sleep(1)
    print 'end'

def outputProxy():
    r = redis.Redis(connection_pool=redisPool)
    proxy=r.rpop("daxiang_proxy")
    while 1:
        if proxy is None:
            time.sleep(1)
            print 'sleep 1'
            proxy=r.rpop("daxiang_proxy")
        else:
            break
    return proxy
startR()
# getProxy()