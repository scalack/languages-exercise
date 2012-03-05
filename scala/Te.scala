abstract class CurrencyZone {
  def make(x: Long): Long
  def age:Int
  abstract class Test{ val name:String}
  val list = List("a", "b") ::: List("c", "d")
  (1 to 5).foldLeft(List[String]())(_ :+ _.toString)
}
class Te extends CurrencyZone{
	 def age:Int = 25
	 def make(x: Long): Long = 2L
	 def foo(a: Any with NotNull) = println(a.hashCode)
}
