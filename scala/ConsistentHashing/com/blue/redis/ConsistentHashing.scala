package com.blue.redis

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-7-27
 * Time: 21:03:12
 * To change this template use File | Settings | File Templates.
 */


import collection.immutable.{TreeMap, SortedMap}

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-7-27
 * Time: 18:11:29
 * To change this template use File | Settings | File Templates.
 */

class ConsistentHashing[T](val hashFunction: HashFunction,val numberOfReplicas: Int){


  private var circle : SortedMap[Long,T] = TreeMap.empty[Long,T]

  def this(hashFunction: HashFunction, numberOfReplicas: Int,nodes: Iterable[T])  {
//  numberOfReplicas 虚拟节点的个数
    this(hashFunction,numberOfReplicas)
    for(node <- nodes)
      add(node)
  }

  def add(node: T){
    for(i <- 0 to numberOfReplicas){
      circle += (hashFunction.hash(node.toString + i) -> node)
    }
  }

  def remove(node: T){
     for(i <- 0 to numberOfReplicas){
      circle -= hashFunction.hash(node.toString + i)
    }
  }

  def get(key: String): Option[T] = {
    if(circle.isEmpty) return None

    var hash =  hashFunction.hash(key);

    if(!circle.contains(hash)){
      val tailMap  : SortedMap[Long,T] =  circle.from(hash)

              if(tailMap.isEmpty)
                hash =  circle.firstKey
              else
                hash =  tailMap.firstKey
    }
    circle.get(hash)
  }



}

object ConsistentHashing{
  def main(args: Array[String]) {
    val list: List[String] = List("192.168.0.246:11212","192.168.0.247:11212","192.168.0.249:11212")
    val hash = new ConsistentHashing(new HashFunction(),3,list)
    //hash.add("192.168.0.248:11212");
    println(hash.get("a"))
  }
}