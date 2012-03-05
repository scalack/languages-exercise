package com.blue

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-2-3
 * Time: 10:38:48
 * To change this template use File | Settings | File Templates.
 */

  trait TAction {
     def doAction
 }

  trait TBeforeAfter extends TAction {
    abstract override def doAction{
      println("/entry before action")
      super.doAction
      println("/exit after action")
    }
  }

  trait TTwiceAction extends TAction {
    abstract override def doAction {
      for(i<- 1 to 2){
        super.doAction
        println("=>No." + i)
      }
    }
  }
class RealAction extends TAction  {
     def doAction = println("** real action done!!**")
}

object Testit{
  def main(args: Array[String]) {
    val act = new RealAction  with TTwiceAction     with TBeforeAfter
    act.doAction
     
  }

}

