
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2009-10-27
 * Time: 10:03:49
 * To change this template use File | Settings | File Templates.
 */
import java.util.{Date,Locale},java.text.DateFormat
import java.text.DateFormat._

object Test  extends Application{
    val now = new Date
    val df = getDateInstance(LONG,Locale.FRANCE)
    println(df format now)
    println("Hello")
    var i:Int = 0
    i+=1
    println(i)
     
}
