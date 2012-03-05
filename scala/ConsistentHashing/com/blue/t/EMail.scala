package com.blue.t

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-6-11
 * Time: 9:32:02
 * To change this template use File | Settings | File Templates.
 */

object EMail {
  def apply(user: String,domain: String) = user + "@" + domain

  def unapply(str: String): Option[(String,String)] = {
    val parts = str.split("@")
    if(parts.length == 2) Some(parts(0),parts(1)) else None
  }
}

case class Person(name: String,isMale: Boolean,children: Person*)

object Test{
  def main(args: Array[String]) {
    val lara = Person("Lara",false)
    val bob = Person("Bob",true)
    val julie = Person("Julie",false,lara,bob)
    val persons = List(lara,bob,julie)
    val p = persons.filter(p => !p.isMale).flatMap(p => (p.children.map(c=>(p.name,c.name))))

    val s =  for{
      q <- persons
      n = q.name
      if (n.startsWith("B"))
    }yield n
    val t = for(x <- List(1,2); y <- List("one","two"))
      yield (x,y)
     println(t)


    
  }
}

