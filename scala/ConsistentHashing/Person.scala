import com.blue.Person

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-2-3
 * Time: 10:14:08
 * To change this template use File | Settings | File Templates.
 */

case class A[T](a:T,b:Int){}
object Person {
  def marry(p1:Person,p2:Person):Unit = {
    p1.spouse = p2
    p2.spouse = p1
  }

  def f(elems: List[Int],x:Int=0,cond:Boolean=true) {}
  def sum[T](a:T,b:T)(implicit m:Numeric[T]):T ={
    import m._
    a + b
  }

  def max[T](a:T,b:T)(implicit m:Numeric[T]) :T={
    import m._
    if(a >= b) a else b

  }
  def main(args: Array[String]) {
    val m = new Person("jacky","wu")
    val w = new Person("joys","Wang")

    f(List(1))
    f(Nil,cond=false)

    val a1:A[Int] = A(1,2)
    val a2:A[String] = a1.copy(a="someString")
    println(sum(1,4))
    println(max(1,2))

//    marry(m,w)
   // m<+w
    //print(w)
    
  }
}