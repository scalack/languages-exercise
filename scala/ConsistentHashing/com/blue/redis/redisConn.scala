package com.blue.redis

import com.redis.Redis
import collection.immutable.TreeMap

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-7-27
 * Time: 10:51:23
 * To change this template use File | Settings | File Templates.
 */

object redisConn  {
  val red = new RedisCluster("localhost:6379","localhost:6380")

   

  def main(args: Array[String]) {

    red.set("name","10")
    val name = red.get("name")
    val withDefault: Option[String] => Int = {
      case Some(x) => x.toInt
      case None => 0
    }
    val p = withDefault(name)
    val q = name match {
      case Some(x) => x.toInt
      case None => 0
    }
    println(red.incr("name"))
    red.setAdd("tom:friend:list","123")
    red.setAdd("tom:friend:list","456")
    red.setAdd("tom:friend:list","789")
    red.setAdd("tom:friend:list","101")
    red.set("uid:sort:123","1000")
    red.set("uid:sort:456","6000")
    red.set("uid:sort:789","100")
    red.set("uid:sort:101","5999")
    red.set("uid:123","{'uid':'123','name':'lucy'}")
    red.set("uid:456","{'uid':'456','name':'jacky'}")
    red.set("uid:789","{'uid':'789','name':'tonny'}")
    red.set("uid:101","{'uid':'101','name':'marry'}")
//    println(red.sort("tom:friend:list by uid:sort:* get uid:* get uid:sort:*"));

    red.zSetAdd("hackers",10,"jacky")
    red.zSetAdd("hackers",1,"atom")
    red.zSetAdd("hackers",5,"tom")
    red.zSetAdd("hackers",2,"william")
    var list = red.zSetRange("hackers",0,-1)
                           

//    println(red.slaveOf("localhost","6380"))
    list = red.zSetReverseRange("hackers",0,-1)
//    println(list)


  }


  
}