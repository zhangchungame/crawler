from bs4 import BeautifulSoup
import requests
session=requests.session()
resp=session.get("http://crm.easyrong.com")
html=resp.text

soup = BeautifulSoup(html)

form=soup.find_all(class_="lock-form")

print form[0].attrs['action']