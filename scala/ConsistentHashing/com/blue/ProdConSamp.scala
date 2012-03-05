package com.blue

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2009-11-14
 * Time: 12:06:10
 * To change this template use File | Settings | File Templates.
 */
import scala.actors._,Actor._
object ProdConSamp {

  case class Message(msg:String)

  def main(args: Array[String]) {
    val consumer =
    actor{
      var done = false
      while(! done){
        receive {
          case msg =>
          println("Recieve message ! ->" + msg)
          done = (msg =="DONE")
          reply("RECEIVED")
        }
      }
    }

    println("Sending ...")

    consumer !? "Mares eat oats"
    //receive { case "RECEIVED" => println("OK") }
    println("Sending ...")
    consumer !? "Does eat oats"
    println("Sending ...")
    consumer !? "DONE"
  }
}