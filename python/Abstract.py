#coding=utf-8
import os
name=2
def abstract(func):
    func.__isabstract__ = True
    return func
    
class Abstract(type):
    def __new__(metaclz, definedclzname, supers, attrs):
        clz = type.__new__(metaclz, definedclzname, supers, attrs)
        # 這個類別自己定義的抽象方法
        abstracts = {name
                     for name, value in attrs.items()
                     if getattr(value, "__isabstract__", False)}
        # 從父類別中繼承下來的抽象方法
        for super in supers:
            for name in getattr(super, "__abstractmethods__", set()):
                value = getattr(clz, name, None)
                # 如果類別有定義自己的方法，則該方法就不會有 __isabstract__
                # 就不會被加入 abstracts
                if getattr(value, "__isabstract__", False):
                    abstracts.add(name)
        
        # 這個類別總共的抽象方法        
        clz.__abstractmethods__ = frozenset(abstracts)
        
        return clz


class Abs(object):
    __metaclass__ = Abstract
    @abstract
    def doSome():
        pass

class SubAbs(Abs):
    def doSome():
        pass


sub = SubAbs()

##
#the spike of using the json module
##
import json

strs = json.dumps(['person',{'names':('jack','tomas')}])
print strs

person = json.loads(strs)
print person[1]['names'][0]











    
