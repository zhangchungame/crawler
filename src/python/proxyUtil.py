#coding:UTF-8

import redis
import requests
import threading
import time
import json

redisPool = redis.ConnectionPool(host='172.16.11.70', port=6379, db=8, password='redis')

def getProxy():
    url="http://tvp.daxiangdaili.com/ip/?tid=557552170840411&num=5&delay=3&category=2&sortby=time&filter=on"
    session=requests.session()
    resp=session.get(url)
    tmp=resp.text.split("\r\n")
    return tmp

def check(proxy):
    print "check start",proxy
    session=requests.session()
    session.proxies={ "http": "http://"+proxy, "https": "http://"+proxy, }
    session.timeout=5
    try:
        resp=session.get("http://ip.chinaz.com/getip.aspx")
        text=resp.text
        print proxy+"==="+text
        if text.find("address")==-1:
            raise Exception("no address",proxy)
        return proxy
    except:
        print "exception"
        return ""
def insert(proxy):
    if check(proxy)!="":
        r = redis.Redis(connection_pool=redisPool)
        r.rpush("daxiang_proxy",proxy)

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
def startR():
    r=redis.Redis(connection_pool=redisPool)
    while 1:
        if(r.llen("daxiang_proxy")<5):
            proxys=getProxy()
            for proxy in proxys:
                t=threading.Thread(target=insert,args={proxy})
                t.start()
        time.sleep(1)
    print 'end'

# startR()
# getProxy()