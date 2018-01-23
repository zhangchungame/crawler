#coding:UTF-8

import requests

session=requests.Session()

result=session.get("http://crm.easyrong.com")
print session.cookies
session

postData={
    'memberPhone':'13681736848',
    'memberPwd':'xlh123456'
}
result=session.post("http://crm.easyrong.com/login",postData)

result=session.post("http://crm.easyrong.com/crm/enterprisedetail/view?reg_credit_no=33032400003473191330324691285112D",postData)

print session.cookies
