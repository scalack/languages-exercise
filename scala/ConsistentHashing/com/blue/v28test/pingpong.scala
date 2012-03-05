package com.blue.v28test

import actors.Actor


case object Ping
case object Pong
case object Stop
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-7-19
 * Time: 17:42:35
 * To change this template use File | Settings | File Templates.
 */

object pingpong extends Application {
  val pong = new Pong
  val ping = new Ping(100000,pong)
  ping.start
  pong.start
}

class Ping(count: Int,pong: Actor) extends Actor{
  def act(){
   var pingsLeft = count - 1
    pong ! Ping
    loop {
      react {
        case Pong =>
        if(pingsLeft % 1000 == 0)
          println("Ping: pong")
        if(pingsLeft > 0){
          pong ! Ping
          pingsLeft -= 1
        }else{
          println("Ping: stop")
          pong ! Stop
          exit()
        }

      }
    }
  }
}

class Pong extends Actor {
  def act(){
    var pongCount = 0
    loop {
      react {
        case Ping =>
        if(pongCount % 1000 == 0)
          println("Pong: ping " + pongCount)
         this.reply(Pong)
        pongCount += 1

        case Stop =>
          println("Pong: stop")
          exit()
      }
    }
  }
}


object seq extends Application {
  import scala.actors.Actor._
  val a = actor {
    {
      react {
        case 'A => println("received 1st message")
      };()
    } andThen react {
        case 'A => println("received 2nd message")
      
    }

  }

  a ! 'A
  a ! 'A

}