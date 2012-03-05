package com.blue

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-2-3
 * Time: 13:37:38
 * to test the for function of the scala-v2.8beta1
 */

object V28Test {
  def time(call : => Unit,count:Int):Long = {
     var cnt = count
     val start = System.currentTimeMillis
    while(cnt > 0){
      call
      cnt -= 1
    }
    System.currentTimeMillis - start
  }
  def main(args: Array[String]) {
     val set = 1 until 1000
     var t =  time({
       var sum = 0
        for(num <- set;if(num%2 == 0)) sum +=num
      },100000)
      print(t)
  }
}