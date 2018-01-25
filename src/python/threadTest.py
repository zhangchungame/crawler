# coding:UTF-8

import threading
import time
import redis


class MyThread(threading.Thread):
    def __init__(self, num):
        threading.Thread.__init__(self)
        self.num = num

    def run(self):  # 定义每个线程要运行的函数
        print("running on number:%s" % self.num)
        time.sleep(3)
        print "self end" + str(self.num)
    def stop(self):
        self.__stop()




t1 = MyThread(1)
t1.start()
# t1.stop()
print 'end aaaa'
