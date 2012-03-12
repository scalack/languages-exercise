package xml

import collection.SortedMap

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 10-12-11
 * Time: 下午3:04
 * To change this template use File | Settings | File Templates.
 */

object Test {
  def main(args: Array[String]) {
    println("heldee")
    var list: List[Int] = List.empty

    var ring = SortedMap[Long, Int]()

    ring += (1L -> 2)
    list = 2 :: list
    println(list)
    assert(testIt(3) == 5)

  }

  def testIt(a: Int): Int = {
    require(a > 1)
    a + 2
  }

  //implicit def any2Repeat (x: Int): Repeation = new Repeation (x)
  //5.times 做{println("he")}


 /* import scala.io.Source

  def widthOfLength(s: String) = s.length.toString.length

  if (args.length > 0) {
    val lines = Source.fromFile(args(0)).getLines.toList
    val longestLine = lines.reduceLeft(
      (a, b) => if (a.length > b.length) a else b
    )
    val maxWidth = widthOfLength(longestLine)
    for (line <- lines) {
      val numSpaces = maxWidth - widthOfLength (line)
      val padding = " " * numSpaces
      print(padding + line.length + " | " + line)
    }
  }
  else
    Console.err.println("Please enter filename")

*/
}


class Repeation(val x: Int) {
  def times: worker = {
    new worker(x)
  }

  def 次 : worker = {
    new worker(x)
  }
}

class worker(val t: Int) {
  def repeat(func: => Unit) {
    for (v <- 0 until t) {
      func
    }
  }

  def 做(func: => Unit) {
    for (v <- 0 until t) {
      func
    }
  }
}