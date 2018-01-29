# coding:UTF-8

import requests
import time
import PyV8
import execjs
import os
import j



resp="""<script>var x="orders@href@xY@@0@catch@Expires@var@addEventListener@5@xGp@__jsl_clearance@UkA@window@try@@setTimeout@join@return@Jan@else@__phantomas@18@1517065873@Array@length@false@pQg@2@replace@if@cookie@attachEvent@_phantom@function@while@3@location@27@1500@onreadystatechange@Path@document@13@QDU@for@e@dc@Sat@@DOMContentLoaded@captcha@challenge@@l@GMT@i@D@766@16@charAt@11@GK@cd".replace(/@*$/,"").split("@"),y="8 1o=14(){15(e.13||e.m){};8 22,1h='c=o.1s|5|';8 1=[((-~[]<<-~[])+[]+[]),[-~~~{}]+[-~[t]],(((-~[]<<-~[])<<(+!-{}))+[[]][~~{}]),[-~~~{}],[-~~~{}]+(~~{}+[[], []][-~~~{}]),[-~~~{}]+(((-~[]<<-~[])<<(+!-{}))+[[]][~~{}]),[-~[t]],[-~~~{}]+[-~~~{}],(16+16+[[]][~~{}]),(~~{}+[[], []][-~~~{}]),((-~!{}|-~!{}-~!{})+a+[]),(-~!{}+[~~[]]-(-~!{})+[[]][~~{}]),(-~!{}+16+16+[]),[(-~[]+[~~!{}])/[-~-~[]]],[-~~~{}]+((-~[]<<-~[])+[]+[])];22=p(1.q);1f(8 1q=5;1q<1.q;1q++){22[1[1q]]=['s',[-~[t]],'3',({}+[]).1u(16-~-~[]+((-~[]<<-~[])<<(+!-{})))+((-~!{}|-~!{}-~!{})+a+[])+(-~!{}+[~~[]]-(-~!{})+[[]][~~{}])+[{}+[]+[]][5].1u([-~~~{}]+(~~{}+[[], []][-~~~{}]))+({}+[]).1u(16-~-~[]+((-~[]<<-~[])<<(+!-{}))),'b','1r',({}+[]).1u(16-~-~[]+((-~[]<<-~[])<<(+!-{}))),(~~{}+[[], []][-~~~{}]),'d','21','1e',(-~!{}+[~~[]]-(-~!{})+[[]][~~{}]),(-~!{}+16+16+[])+[(-~[]+[~~!{}])/[-~-~[]]],[[][[]*{}]+[[], []][-~~~{}]][5].1u(-~!{}),'%'][1q]};22=22.i('');1h+=22;h('17.2=17.2.u(/[\\?|&]1l-1m/,\\'\\')',19);1c.11=(1h+';7=1i, 18-k-n 1t:20:1d 1p;1b=/;');};10((14(){f{j !!e.9;}6(1g){j r;}})()){1c.9('1k',1o,r);}l{1c.12('1a',1o);}",z=0,f=function(x,y){var a=0,b=0,c=0;x=x.split("");y=y||99;while((a=x.shift())&&(b=a.charCodeAt(0)-77.5))c=(Math.abs(b)<13?(b+48.5):parseInt(a,36))+y*c;return c},g=y.match(/\b\w+\b/g).sort(function(x,y){return f(x)-f(y)}).pop();while(f(g,++z)-x.length){};eval(y.replace(/\b\w+\b/g, function(y){return x[f(y,z)-1]}));</script>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
"""
resp=resp[9:]
tmp=resp.split('</script')
resp=tmp[0]
resp=resp.replace("eval(y.replace", "var aaa=(y.replace");
resp = resp + "aaa=aaa.replace(\"while(window._phantom||window.__phantomas){};\", \"\");bbb = aaa.split(\"setTimeout\");aaa = bbb[0] + \"return dc;}\";aaa = aaa.replace(\"var l=\", \"\");";

# aaa=execjs.get().name # this value is depends on your environment.
# os.environ["EXECJS_RUNTIME"] = "Node"
# aaa=execjs.get().name
#
# aaa=default = execjs.get() # the automatically picked runtime
# aaa=default.eval("1 + 2")

import execjs.runtime_names
# jscript = execjs.get(execjs.runtime_names.JScript)
# jscript.eval("1 + 2")
#
# import execjs.runtime_names
node = execjs.get(execjs.runtime_names.Node)
node.eval(resp)



#
# ctx = execjs.compile("""
# ...     function add(x, y) {
# ...         return x + y;
# ...     }
# ... """)
# >>> ctx.call("add", 1, 2)
print 1