package com.blue.t

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-6-10
 * Time: 16:13:17
 * To change this template use File | Settings | File Templates.
 */
import util.continuations._

object ContTest {
  def main(args: Array[String]) {

    val z = reset{
      2 * {
        println("up")
        val x = shift((k:Int=>Int)=>k(k(k(8))))
        println("down")
        x
      }
    }
    println(z)
  }
}