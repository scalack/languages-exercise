trait DoStuff { def doStuff(): Unit }
class Foo {
class B
def doStuff() = { println("foo") }
}
trait Bar extends DoStuff {
self: Foo =>
abstract override def doStuff() = {
println(super.toString)
println(List(new B()))
super.doStuff()
println("bar")
}
}

object Test{
def main(args:Array[String]) =
  (new Foo with Bar).doStuff
  
}