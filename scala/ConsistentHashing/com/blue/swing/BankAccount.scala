package com.blue.swing

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-6-5
 * Time: 16:20:27
 * To change this template use File | Settings | File Templates.
 */
object BankAccount {
  def main(args: Array[String]) {
    val account = new BankAccount 

    account deposit 10
    account deposit 100
    account withdraw 20
    println(account.getBalance)

  }
}



class BankAccount{
  private var balance : Int = 0
  def getBalance : Int = balance

  def deposit(amount : Int){
    require(amount > 0)
    balance += amount
  }

  def withdraw(amount : Int) : Boolean =
  if(amount <= balance) {
    balance -= amount ;
    true
  } else
   false
}

