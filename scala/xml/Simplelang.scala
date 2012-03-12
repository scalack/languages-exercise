package xml

import util.parsing.combinator.RegexParsers
import util.matching.Regex

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 10-12-21
 * Time: 下午2:42
 * To change this template use File | Settings | File Templates.
 */
sealed  abstract class Statment
case class Print(s:String) extends Statment
case class Millis() extends Statment

object Simplelang extends RegexParsers {
  def until(r:Regex):Parser[String] = new Parser[String]{
    def apply(in:Input) = {
      val source = in.source
      val offset = in.offset
      val start = offset
      (r.findFirstMatchIn(source.subSequence(offset,source.length))) match{
        case Some(matched) =>  Success(source.subSequence(offset,offset + matched.start).toString,in.drop(matched.start))
        case None => Failure("string matching regex '" + r + "' expected but '" + in.first + "' found ",in.drop(0))
      }
    }
  }

  def until(s:String ):Parser[String] = until(java.util.regex.Pattern.quote(s).r)

  def interpret(stats:List[Statment]):Unit = stats match  {
    case Print(s) :: rest => {
      print(s)
      interpret(rest)
    }
    case Millis() :: rest =>{
       print(System.currentTimeMillis)
      interpret(rest)
    }
    case Nil => ()
  }

  def apply(input:String): List[Statment] = parseAll(beginning,input) match  {
    case Success(tree,_) => tree
    case e:NoSuccess => throw new RuntimeException("Syntax error: " + e)
  }
   /** GRAMMAR **/

  def beginning = (
    "[[" ~> stats |
    until("[[") ~ "[[" ~ stats ^^ {
      case s ~ _ ~ ss => Print(s) :: ss
    }
  )

  def stats = rep1sep(stat, ";")

  def stat = (
    "millis" ^^^ { Millis() } |
    "]]" ~> ( (until("[[") <~ "[[") | until("\\z".r)) ^^ {
      case s => Print(s)
    }
  )

  def main(args: Array[String]) {
    val tree  = Simplelang("now:[[ millis;]]\n and now:[[;millis;]]")
    println(tree)
    interpret(tree)
    val a:List = List(1,2,3).equals
    println(a.addString())
    println(Array(1,2).equals()==Array(1,2))
  }
}
