#coding:UTF-8
import redis
import jpype
import pymysql
from DBUtils.PooledDB import PooledDB

redisPool = redis.ConnectionPool(host='172.16.11.70', port=6379, db=8,password='redis')
# redisPool = redis.ConnectionPool(host='localhost', port=6379, db=8)

jpype.startJVM(jpype.getDefaultJVMPath(), "-ea", "-Djava.class.path=/code/java/forpython/target/classes/")

pool = PooledDB(pymysql,5,host='172.16.11.135',user='root',passwd='root',db='xlh_crawler',port=3306,charset='utf8',cursorclass=pymysql.cursors.DictCursor) #5为连接池里的最少连接数
