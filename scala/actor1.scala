import scala.actors.Actor._
import scala.actors.Actor
 case class Set(key:String,value:String)
class Write extends Actor{
 
  def act = {

    react{
      case s:Set => print(s)
      case _ => println("else")
    }
  }
  
}
object Test{
def main (args: Array [String]) {
    val w = new Write
    w start()
    w ! Set("jack","tomas")
  }
}
