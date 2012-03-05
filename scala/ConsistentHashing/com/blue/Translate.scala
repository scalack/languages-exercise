package com.blue

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-5-24
 * Time: 20:34:18
 * To change this template use File | Settings | File Templates.
 */

import java.io.{InputStreamReader, BufferedReader}
import scala.actors.Actor._
import com.google.api.translate.{Language, Translate}

object Translates   {
  def main(args: Array[String]) {
    
    val echoActor = actor {
      def execute(){
        val onJob = receive {
          case "quit" =>
          false
          case msg : String =>
          println(Translate.translate(msg,Language.CHINESE_SIMPLIFIED,Language.ENGLISH))
          true
        }
        if(onJob) execute()
      }
      execute()
      
    }

    val reader = new BufferedReader(new InputStreamReader(System.in))
    def translate(){
      val word = reader.readLine
      echoActor ! word
      if(word !="quit") translate()
    }
    translate()
  }


}