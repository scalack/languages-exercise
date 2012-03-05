package com.blue.t

import java.util._
import xml.Node
import scala.collection.immutable.List
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-2-3
 * Time: 13:59:23
 * 实存(Existantial)类型,  结构（Structural）类型 ,复合(Compound)类型
 */

trait ExistClass {
  var s1:List[String] = List()
  def foo(callable: { def call: Unit })
}

object AppleCounter{
  def main(args: Array[String]) {
//    var appleList = (0 until 100).reverse.map(x => x match {
//      case 1 => "1 apple on the wall"
//      case 0 => "no apple on the wall"
//      case _ => x + "apples on the wall"
//    })
//    appleList.foreach(println _)

   val xml = <xml>
      <dir name="com">
        <dir name="mamezou">
          <file name="aaa.txt"></file>
          <file name="bbb.txt"></file>
          </dir>
        <file name="ccc.txt"></file>
        </dir>
        <file name="ddd.txt"></file>
     </xml>

    fileFinder(xml).foreach(x =>  println("file:" + x.attribute("name").getOrElse("")))
  }


// xml 处理
  def fileFinder(node:Node):List[Node] = node.label match{
    case "xml" => node.child.toList.flatMap(fileFinder)
    case "dir" => node.child.toList.flatMap(fileFinder)
    case "file" => List(node)
    case _ =>  List()
  }

}
