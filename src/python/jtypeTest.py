#coding:UTF-8
import jpype
from jpype import *
import requests,time
import threading

jpype.startJVM(jpype.getDefaultJVMPath(), "-ea", "-Djava.class.path=/code/java/forpython/target/classes/")


def getChallenge(tNum):
    session=requests.session()
    loginurl="http://www.gsxt.gov.cn/SearchItemCaptcha"
    result=session.get(loginurl)
    # dowJvm(result.text)
    jpype.attachThreadToJVM()
    print jpype.isThreadAttachedToJVM()
    A = jpype.JClass("com.GovTest")
    Aobj=A()
    print  str(tNum)+Aobj.challenge(result.text)

aaa="<script>sdfsdf"
if "<scrisdfpt>" not in aaa:
    print "no"
    raise Exception("被屏蔽了")
else:
    print "y"


#
# for i in range(3):
#     t=threading.Thread(target=getChallenge,args={i})
#     t.start()
#     # time.sleep(1)
# while 1:
#     time.sleep(1)
#
# print 'end'
# jpype.shutdownJVM()