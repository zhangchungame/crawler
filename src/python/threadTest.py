#coding:UTF-8

import threading
import time
import redis


pool = redis.ConnectionPool(host='172.16.11.70', port=6379, db=8,password='redis')
r = redis.Redis(connection_pool=pool)

r.set("aaa","bbb")
print r.get("aaa")
r.rpush("sdf","ddd")
hello="helo"
def mythread():
    print hello

def main():
    t=threading.Thread(target=mythread)
    t2=threading.Thread(target=mythread)
    t.start()
    t2.start()
    print "end"

main()