#coding=utf-8
def next(pattern):
    p_len = len(pattern)
    pos = [-1]*p_len
    j = -1
    for i in range(1,p_len):
        while j > -1 and pattern[j+1] != pattern[i]:
            j = pos[j]
        if pattern[j+1] == pattern[i]:
            j = j + 1
            pos[i] = j
    return pos

def kmp(ss,pattern):
    pos = next(pattern)
    ss_len = len(ss)
    pattern_len = len(pattern)
    j = -1
    for i in range(ss_len):
        while j > -1 and pattern[j+1] != ss[i]:
            j = pos[j]
        if pattern[j+1] == ss[i]:
            j = j + 1
            if j == pattern_len -1:
                print 'matched @: %s' % str(i-pattern_len+1)
                j = pos[j]
            
kmp(u'深圳自来水来自深圳',u'深圳')
