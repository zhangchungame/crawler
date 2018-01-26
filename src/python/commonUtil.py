#coding:UTF-8

import time
import  threading
class ThreadTimeOut(threading.Thread):
    threads=[]
    def run(self):  # 定义每个线程要运行的函数
        try:
            self.threadTimeOut()
        except Exception,e:
            print e.message

    def __init__(self, threads):
        threading.Thread.__init__(self)
        self.threads = threads
    def threadTimeOut(self):
        while 1:
            # print 'threadTimeOut threads len='+str(len(self.threads))
            for i in reversed(range(len(self.threads))):
                thread=self.threads[i]
                if thread.isAlive():
                    thread.dealTimeOut()
                else:
                    del self.threads[i]
            time.sleep(2)
