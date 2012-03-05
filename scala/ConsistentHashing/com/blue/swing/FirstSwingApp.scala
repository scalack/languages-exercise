package com.blue.swing

import scala.swing._
import collection.immutable.SortedSet


/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-6-5
 * Time: 11:28:01
 * To change this template use File | Settings | File Templates.
 */

object FirstSwingApp    extends SimpleSwingApplication{
  def newField = new TextField{
    text = "0"
    columns = 5
  }

  val celsius = newField
  val fahrenheit = newField

  def top = new MainFrame {
      title = "Convert Celsius / Fahrenheit"
      SortedSet("hello","world")
      contents = new FlowPanel(celsius,new Label("Celsius="),fahrenheit,new Label("Fahrenheit"))  
  }

}