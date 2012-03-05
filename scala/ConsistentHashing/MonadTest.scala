/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-9-1
 * Time: 10:21:47
 * To change this template use File | Settings | File Templates.
 */
//定义表达式
trait Expr {
  def eval:Option[Int]
}

case class  Num(n:Int) extends Expr{
  def eval = Some(n)
}

case class Neg(e:Expr) extends Expr{
  def eval = e.eval.map(-_)
}

case class Add(e1:Expr,e2:Expr) extends Expr{
  def eval = e1.eval.flatMap(n1=>e2.eval.map(n2=>n1+n2))
}

case class Div(e1:Expr,e2:Expr) extends Expr{
  def eval = e2.eval.flatMap(n2 => n2 match{
    case 0 => None
    case _ => e1.eval.flatMap(n1 => Some(n1/n2))
  })
}

/*case class Or(e1:Expr,e2:Expr) extends Expr{
  def eval = e1.eval ++ e2.eval
}*/

object MonadTest    {
  def main(args: Array[String]) {
    print(Add(Neg(Num(5)), Num(1)).eval.get)
  }
}