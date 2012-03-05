package com.blue.redis

import collection.mutable.ArrayBuffer
import com.redis.Redis
import collection.immutable.SortedMap

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-7-28
 * Time: 15:36:45
 * To change this template use File | Settings | File Templates.
 */

trait ConsistentHashRing {
  val replicas: Int

  var sortedKeys: List[Long] = List()
  var cluster = new ArrayBuffer[Redis]
  var ring = SortedMap[Long, Redis]()
  val hashFunction = new HashFunction
  // Adds the node to the hashRing
  // including a number of replicas.
  def addNode(node: Redis) = {
    cluster += node
    (1 to replicas).foreach{ replica =>
      val key = hashFunction.hash(node+":"+replica)
      ring += (key -> node)
      sortedKeys = sortedKeys ::: List(key)
    }
    sortedKeys = sortedKeys.sortWith(_ < _)
  }

  def delNode(node: Redis) = {
    cluster -= node
    (1 to replicas).foreach{ replica =>
      val key = hashFunction.hash(node+":"+replica)
      ring -= key
      sortedKeys = sortedKeys -- List(key)
    }
    sortedKeys = sortedKeys.sortWith(_ < _)
  }

  // get the node in the hash ring for this key
  def getNode(key: String): Redis = {
       if(ring.isEmpty) return new Redis()

    var hash =  hashFunction.hash(key);

    if(!ring.contains(hash)){
      val tailMap  : SortedMap[Long,Redis] =  ring.from(hash)

              if(tailMap.isEmpty)
                hash =  ring.firstKey
              else
                hash =  tailMap.firstKey
    }
    ring.get(hash) match {
      case Some(x) => x
      case None => new Redis()
    }
    
  }
}