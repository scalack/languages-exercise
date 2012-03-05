#coding=utf-8
import c
#function wrapper
print dir()
def wrap():
   x = 10
   def fun():
      return x

   return fun

f = wrap()

def decore1(d):
   return lambda:d() + 20

def decore2(a):
   return lambda d:d()*a

@decore2(2)
def fu():
   return 2
print fu

def sidedish(number):
    return {
        1 : lambda meal: (lambda: meal() + 30),
        2 : lambda meal: (lambda: meal() + 40),
        3 : lambda meal: (lambda: meal() + 50),
        4 : lambda meal: (lambda: meal() + 60)
    }.get(number, lambda meal: (lambda: meal()))

@sidedish(2)
@sidedish(3)
def friedchicken():
    return 49.0
   
print(friedchicken()) # 139.0


class decore:
   def __init__(self,fun):
      self.fun = fun

   def __call__(self):
      return self.fun()+30

@decore
def some():
   return 20
print some()


class Decor:
   def __init__(self,clz):
      self.clz = clz

   def __call__(self,*args):
      class Wrapper:
         def __init__(self,me):
            self.me = me

      return Wrapper(self.clz(*args))

@Decor
class Some:
   def __init__(self,name):
      self.name = name

print Some('jack').me.name




 
          
#__get__(需要显式继承object）
class Descriptor(object):
    def __get__(self, instance, owner):
        print(self, instance, owner)
        print(2)
    def __set__(self, instance, value):
        print(self, instance, value)
        print(1)
    def __delete__(self, instance):
        print(self, instance)

class Some(object):
    x = Descriptor()

s = Some()
s.x
s.x = 10    


def __a__(self,arg):
   print("__init__")
   self.arg = arg

so = type('So',(object,),{'__init__':__a__})
print so


