#coding:UTF-8
import re

import pymysql
import requests,time
from DBUtils.PooledDB import PooledDB
from bs4 import BeautifulSoup
pool = PooledDB(pymysql,5,host='localhost',user='root',passwd='root',db='ceshi',port=3306,charset='utf8',cursorclass=pymysql.cursors.DictCursor) #5为连接池里的最少连接数
conn = pool.connection()
cursor=conn.cursor()
session=requests.session()
#爬取相册链接
def getAlbumUrl(pageurl):
    time.sleep(0.2) #间隔0.2秒，防止把网站爬挂掉
    print "start url="+pageurl
    resp=session.get(pageurl)
    resp.encoding='gb2312'  #告诉session解析编码，将gb2312解析成unicode
    html=resp.text
    soup=BeautifulSoup(html,"html.parser")
    itemBs=soup.find_all(class_="item_b")
    for itemB in itemBs:
        try:
            a=itemB.find("a")
            comment=itemB.find(class_="items_comment")
            aaa=comment.find_all("a")
            aaa[1]['title']
            sql="insert into xjj_album (title,cate,url) values('"+a.contents[0]+"','"+aaa[1]['title']+"','"+a['href']+"')"
            cursor.execute(sql)
            conn.commit()
        except:
            print 'mysql异常'  #数据库对url字段加了唯一索引，防止重复爬取

def getImagesUrl():
    sql="select max(album_id) from xjj_photo"   #从上次爬取到的最后一次的相册ID开始抓取
    cursor.execute(sql)
    ids=cursor.fetchall()
    maxId=ids[0]['max(album_id)']
    if maxId is None:
        maxId=0
    sql="select * from xjj_album where id>="+str(maxId)
    cursor.execute(sql)
    albums=cursor.fetchall()
    for album in albums:
        findImagesUrl(album['id'],album['url'])  #循环相册，抓取图片url


def findImagesUrl(albumId,url):
    time.sleep(0.1)
    print "start url="+url
    try:
        resp=session.get(url)
        resp.encoding='gb2312'
        html=resp.text
        soup=BeautifulSoup(html,"html.parser")
        image=soup.find(class_="big-pic")
        srcurl=image.img['src']
    except:
        print "网页不存在"
    try:
        sql="insert into xjj_photo (album_id,url)values("+str(albumId)+",'"+srcurl+"')"
        print sql
        cursor.execute(sql)
        conn.commit()
    except:
        print 'mysql 异常'
    nextpage=soup.find(id="nl")     #分析下一页按钮的代码，如果是##说明是最后一页
    if nextpage!=None:
        nexturl=nextpage.a['href']
        if nexturl!='##' :
            nexturl=re.sub("(\d*|\d*_\d*)\.html",nexturl,url)       #正则替换
            findImagesUrl(albumId,nexturl)



if __name__ == "__main__":
    for i in range(1,774):
        url="http://www.mmonly.cc/mmtp/list_9_"+str(i)+".html"
        # getAlbumUrl(url)




    getImagesUrl()