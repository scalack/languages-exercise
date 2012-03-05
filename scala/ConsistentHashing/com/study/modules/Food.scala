package com.study.modules

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-7-29
 * Time: 9:58:22
 * To change this template use File | Settings | File Templates.
 */

abstract class Food(val name: String) {
  override def toString() = name
}

object Apple extends Food("Apple")
object Orange extends Food("Orange")
object Cream extends Food("Cream")
object Sugar extends Food("Suger")

class Recipe(val name: String,val ingredients: List[Food],val instructions: String){
  override def toString() = name
}

object FruitSalad extends Recipe("FruitSalad",List(Apple,Orange,Cream,Sugar),"stir it all together")

object SimpleDatabase{
  def allFoods = List(Apple,Orange,Cream,Sugar)
  def foodNames(name: String):Option[Food] =
    allFoods.find(_.name == name)

  def allRecipes: List[Recipe] = List(FruitSalad)
}

object SimpleBrower{
  def recipesUsing(food: Food) =
     SimpleDatabase.allRecipes.filterNot(! _.ingredients.contains(food))

  def main(args: Array[String]) {
    val apple = SimpleDatabase.foodNames("Apple").get
    println(apple)
    println(SimpleBrower.recipesUsing(apple))
  }

}

