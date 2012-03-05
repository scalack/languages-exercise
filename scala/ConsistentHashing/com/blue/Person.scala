package com.blue

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-2-3
 * Time: 9:14:04
 * To change this template use File | Settings | File Templates.
 */

class Person(val fn: String, var ln: String, var spouse: Person)   {
  def this(fn: String, ln: String) = {this (fn, ln, null)}
  def introduction(): String =
    return "我的名字是" + ln + " " + fn +
            (if (spouse != null) "对方的名字是" + spouse.ln + " " + spouse.fn + "。" else "。")

  def getMarriedTo(p:Person) {
    this.spouse = p
    p.spouse = this
  }

  def <+(p:Person):Person = {
    this.getMarriedTo(p)
    p.ln = this.ln
    this
  }
  override def toString:String = super.toString + "[姓：" + ln +
    " 名：" + fn + " 配偶:" + (if (spouse != null) " ("+ spouse.ln + "," + spouse.fn + ")" else "没有") + "]"

}

