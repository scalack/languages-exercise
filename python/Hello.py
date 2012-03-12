#coding=utf-8
'''
Created on 2011-5-31

@author: Administrator
'''
def main(offset=6):
    string = u'静夜思 李白床前明月光，疑似地上霜。举头望明月，低头思故乡。090131'
    a = [[' ']*offset for row in xrange(offset)]
    for i in xrange(offset):
        for j in xrange(offset):
            a[j] = string[j + i*offset]
            b = [[r[col] for r in a[::-1]] for col in xrange(len(a[0]))]
        print '\n'.join([u'┊'.join(unicode(c) for c in row)for row in b])

 
main(6) 


###reduce 函数
s = xrange(100)

s = reduce(lambda x,y:x**y,s)