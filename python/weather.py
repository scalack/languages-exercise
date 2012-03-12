#coding=utf-8
'''
Created on 2011-1-11

@author: Administrator
'''
import urllib,sys,re,redis
province = raw_input('输入省名（请使用拼音）：')
major = raw_input('输入市名（请使用拼音）：')
url = "http://qq.ip138.com/weather/" + province + '/' + major + '.htm'
print url


weatherhtml = urllib.urlopen(url)
result = weatherhtml.read().decode('GB2312')
f = file('weather.txt','a')
 


pattern = 'Title.+<b>(.+)</b>'
Title = re.search(pattern,result).group(1)

pattern = '>(\d*-\d*-\d*.+?)<'
date = re.findall(pattern,result)

pattern = 'alt="(.+?)"'
weather = re.findall(pattern,result)

pattern = '<td>([-]?\d{1,2}.+)</td>'
temperature = re.findall(pattern,result)

print '%35.30s'%Title,""
f.write(Title.encode('GB2312') + "\n")


length = len(date)
red = redis.Redis(host='localhost', port=6379)
key = province + "_" + major


for i in range(length):
    print '%30.20s'%date[i],'\t%s'%weather[i],'\t%s'%temperature[i]
    content =  ('%s'%date[i] + '\t%s'%weather[i] + '\t%s'%temperature[i]).encode('GB2312') + "\n" 
    f.write(content)
    red.set(key+date[i].split(" ")[0],(weather[i] + " " + temperature[i]))

red.save() 

print "age:",red.get(key+"2011-1-11")
 
f.close()

