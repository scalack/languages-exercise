package com.blue.v28test

import concurrent.MailBox

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-7-25
 * Time: 13:57:56
 * To change this template use File | Settings | File Templates.
 */
object onePlaceBuffer {
  import concurrent.ops

  class OnePlaceBuffer{
    private val m = new MailBox(){};
    private case class Empty()
    private case class Full(x: Int)

    m.send(Empty())

    def write(x: Int){
      m.receive {
        case Empty() =>
          println("put " + x)
          m send Full(x)
      }
    }

    def read: Int = m receive{
      case Full(x) =>
      println("get " + x)
      m send Empty();
      x
    }

    }

    def kill(delay: Int) = new java.util.Timer().schedule(
    new java.util.TimerTask{
      override def run() = {
        println("[killed]")
        exit(0)
      }
    }
     ,delay)

    def main(args: Array[String]) {
      val buf = new OnePlaceBuffer
      val random = new java.util.Random()

      def producer(n: Int) {
        Thread.sleep(random.nextInt(1000))
        buf.write(n)
        println(n + 1)

      }

      def consumer{
        Thread.sleep(random.nextInt(1000))
        val n = buf.read
        consumer
      }

      ops.spawn(producer(0))
      ops.spawn(producer(1))
      ops.spawn(consumer)
      kill(10000)
    }
  }