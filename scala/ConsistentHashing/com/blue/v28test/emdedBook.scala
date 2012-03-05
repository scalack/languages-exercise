package com.blue.v28test

import xml.Node
import java.net.URL
import mobile.Location

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-7-19
 * Time: 16:58:52
 * To change this template use File | Settings | File Templates.
 */

object embeddedBook {
  val company = <a href="http://acme.org">ACME</a>
  val first = "Burak"
  val last = "Emir"
  val location = "work"

  val embBook =
    <phonebook>
      <descr>
        This is the <b>phonebook</b> of the
        {company} corporation
      </descr>
      <entry>
         <name>{first + " " + last}</name>
         <phone where={ location }>+41 21 693 68 {val x=60 + 7; x} </phone>
      </entry>
    </phonebook>;


  def add(p: Node,newEntry: Node): Node = p match {
    case <phonebook>{ch @ _*}</phonebook> =>
      <phonebook>{ch}{newEntry}</phonebook>
  }

  val pb2 =
    add(embBook,
      <entry>
        <name>Kim</name>
        <phone where="work">+41 21 111 11 11</phone>
      </entry>);

  def main(args: Array[String]) {
//    println(pb2)
    val url = new URL("http://scala.epfl.ch/classes/examples.jar")
    val location = new Location(url)
    val obj = location create "examples.sort"
    val ar = Array(6,2,8,5,1)
    obj[Array[Int],Unit]("println")(ar)
    obj[Array[Int],Unit]("sort")(ar)
    obj[Array[Int],Unit]("println")(ar)
    println
  }
}