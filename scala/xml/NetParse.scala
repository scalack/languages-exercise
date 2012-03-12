package xml

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import org.xml.sax.InputSource
import xml.parsing.NoBindingFactoryAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}
import xml.NodeSeq

import io.Source
import com.fotolog.redis.RedisClient
import com.fotolog.redis.Conversions._
import java.net.URL
import com.twitter.json.{JsonObject, Json}

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 10-12-21
 * Time: 上午10:25
 * To change this template use File | Settings | File Templates.
 */

class NetParse extends  HttpServlet{


    override def doGet(req : HSReq, resp: HSResp) = {
      var ip = NetParse.getRemortIP(req)
      println(ip)
      ip = "218.17.227.162"
//      val citys  = NetParse.getCityID(ip).toList
//      val city = if(citys.size != 0) citys(0).toString else ""
//      NetParse.initRedis("localhost",6379)

      val ws =  NetParse.getWeath(NetParse.getCityID(ip))("weatherinfo")

      resp.setCharacterEncoding("utf-8")
      resp.setContentType("text/html")
      resp.getWriter().print("您的IP是:" + ip + "<br/> 所在城市是:" + ws("city") +  "<br/> 今天是 :" + ws("date_y") + " "
        + ws("date")  + " " + ws("week") + "<br/>"
        + "今天的天气是:" + ws("weather1") + "<br/>"
        + "明天的天气是:" + ws("weather2"))

    }

    override def doPost(req : HSReq, resp: HSResp) =
    doGet(req,resp)
}




object NetParse         {

  def main(args: Array[String]) {
      initRedis("",1)
//   print(getCityID("172.16.1.122"))
     val r = RedisClient("localhost", 6379)
//     val js =  Json.build(Array(Map("1" -> 2))).toString
//     println(js)
      r.set("name","jackys")
    println(r.get("name") match{
      case Some(x) => new String(x,"utf-8")
    })
     val ip = "218.17.227.162"
     val weather =  NetParse.getWeath(NetParse.getCityID(ip))("weatherinfo")
      println(weather)

}

  def getWeath(cont:String):Map[String,Map[String,String]]  =  {
    val r = RedisClient("localhost", 6379)
    var id = ""

    r.keys("c*").toList.foreach{
      x =>
       val vl = r.get(x) match {
          case Some(x) => tostr(x)
        }
       if (cont.contains(vl))
          id = x
   }

    val url = new URL("http://m.weather.com.cn/data/" + id.substring(1) + ".html")
    val contents = Source.fromURL(url,"utf-8").mkString
     Json.parse(contents) match {
       case map:Map[String,Map[String,String]] => map
     }

  }



   def initRedis(host:String,port:Int) = {
    val c = RedisClient("localhost", 6379)

    Source.fromFile("E:\\IdeaProjects\\TestV10\\citys").getLines.foreach(
      a => {
       var vs  =  a.split("=")
       if(vs.length == 2){

         c.set("c"+vs(0),vs(1))

       }

    })
    c.save

   }

   def tostr(str:String)= {
     new String(str ,"utf-8")
   }
   def getCityID(ip:String) = {


    val parserFactory = new SAXFactoryImpl
    val parser = parserFactory.newSAXParser
    val source = new InputSource("http://ip168.com/ip/?ip="+ ip)
    val adapter = new NoBindingFactoryAdapter
    val nodes:NodeSeq =  adapter.loadXML(source,parser)

    (nodes\\"form"\\"div" last).text

    "深圳"
   }
   def getRemortIP(request:HttpServletRequest):String =  {
    if (request.getHeader("x-forwarded-for") == null) {
     return request.getRemoteAddr();
    }
    return request.getHeader("x-forwarded-for");
 }


}

