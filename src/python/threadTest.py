#coding:UTF-8

import threading
import time
import redis


pool = redis.ConnectionPool(host='localhost', port=6379, db=0)
r = redis.Redis(connection_pool=pool)


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