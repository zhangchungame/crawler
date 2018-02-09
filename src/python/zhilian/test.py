#coding:UTF-8

import requests

session=requests.session()
session.headers={
    'Host': 'rd2.zhaopin.com',
    'Connection': 'keep-alive',
    'Cache-Control': 'max-age=0',
'Upgrade-Insecure-Requests': '1',
'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36',
'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
'Referer': 'https://rd2.zhaopin.com/s/resuadmi/vacancyList.asp',
'Accept-Encoding': 'gzip, deflate, br',
'Accept-Language': 'zh-CN,zh;q=0.9',
}
cookies={
    'dywea':'95841923.53410445889142520.1518077968.1518077968.1518077968.1',
    'dywec':'95841923',
    'dywez':'95841923.1518077968.1.1.dywecsr=(direct)|dyweccn=(direct)|dywecmd=(none)|dywectr=undefined',
                'Hm_lvt_38ba284938d5eddca645bb5e02a02006':'1518077969',
                '__utma':'269921210.375373272.1518077969.1518077969.1518077969.1',
                '__utmc':'269921210',
                '__utmz':'269921210.1518077969.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)',
                '__zpWAM':'1518077969316.479248.1518077969.1518077969.1',
                '__zpWAMs1':'1',
                '__zpWAMs2':'1',
                'Hm_lpvt_38ba284938d5eddca645bb5e02a02006':'1518077980',
                'Token':'39f0c45913dd4caeb559350de50d2f53',
                'uiioit':'3d672038046a55644364496f5c675038006a52644f64446f536726387d6a59644464486f5b675d380b6a54644f64426f53677',
                'at':'39f0c45913dd4caeb559350de50d2f53',
                'rt':'ca17e4e8cd9046808b579601b1584c01',
                'JsNewlogin':'149449684',
                'cgmark':'2',
                'NewPinLoginInfo':'',
                'NewPinUserInfo':'',
                'isNewUser':'1',
                'RDpUserInfo':'',
                'utype':'2',
                'ihrtkn':'',
                'NTKF_T2D_CLIENTID':'guestC9502875-D382-512A-0A62-7487F1AE0BFD',
                'nTalk_CACHE_DATA':'{uid:kf_9051_ISME9754_38392082,tid:1518078521774174}',
                'RDsUserInfo':'24342E6955715A7940320A754D6A5F710D6A5F68496B4D7409333979246B4C340E691D71197947320B754A6A5271066A5968496B4B7450334E793F6B3F345769893EEDEF38BA0875376A2571096A5268336B3D740F334679526B423453695A715B794C320175426A29717A6A54687B250E19D37CF3EF26E3907B0509B908EB1BD5B2CE3B700D3BE7583B913B4A6B2D747F334879516B343427695571137912325975396A13715D6A07681D6B16745C331279026B103404690F71457917325D75426A3871606A5468406B427473332179576B4334476950714B7945320575436A5871016A5268356B3D740F334679526B423453695A715B794C320175426A2F717A6A54687B250E19D37CF3EF26E3907B0509B908EB1BD5B2CE3B700D3BE7583B913B4A6B35747F3348795A6B41345A695971507937327775446A5E71076A51684A6B38747E3348795A6B4A343F69297156794F327075386A5671776A2A68426B417401334C79586B413452695A7152794F327775386A5671776A2A68426B417401334C79586B413452695A7152794F3277753A6A5671046A5268226B30740F334679516B38343A695571597940320175576A5A71056A5B684A6B2C74663348795B6B41345B6953718',
                'SearchHead_Erd':'rd',
                'FSSBBIl1UgzbN7N443S':'hnrvPg6uXUTzBAAD.1uhbKYHVq7CA5KCP.UtWPHpqzOXERb2W5NoJmvBC9fbfj_y',
                'FSSBBIl1UgzbN7N80S':'Jxzz2SDtEsfSVcI.tbz7K2WprDtRWFnRqGovPK9cbFx7dfGhceSpRomPaM4JbBbX',
                '__utmt':'1',
                'dyweb':'95841923.36.7.1518079233069',
                '__utmv':'269921210.|2=Member=149449684=1',
                '__utmb':'269921210.11.6.1518078019925',
                'getMessageCookie':'1',
                'FSSBBIl1UgzbN7N443T':'1LBmgABdxtENV6FqOHY1k3l_cEp1gcmFUHxj9Z4QeMrILaXnNrJgVr5xcjt4KS7u9EKan8afzErtKxewQXkk.hmn9wNQ9xoilKSK4ImtKLcw1PZ5nofM3zOnZrd_pHoItdGv2q14yYxGwM_LedZh.JrYIdBcvxfsXcRDzaUjxlvlsk6pCKa3adcfvHKko7wC51MVpShR6hh2X5eHCuQoxL7TvjyOMw1z8j43TxIAYY4vjl9vWAtljPwfeN9dZf00XxVGxRNidDNcbowF2Up7IrQFxBVEvL4cQptNoLFpYj3dXWH_l2cP2t7GZV0feJiZG9PnuWe3vkof0H_2xvw7bF236qe41QzetDbuYnsP5_Ah.5q'
}
for key in cookies:
    session.cookies[key]=cookies[key]
resp=session.get("https://rd2.zhaopin.com/RdApply/Resumes/Apply/index")
print resp.text