import actors.Actor
import scala.actors.Actor._
import scala.util.Random

/**
 *  猜數字伺服器回傳的結果
 *
 *  @param  x   使用者輸入的答案
 *  @param  a   猜數字遊戲裡的A（數字對，位置也對）
 *  @param  b   猜數字遊戲裡的B（數字對，但位置錯）
 */
case class Status(x: String, a: Int, b: Int)

/**
 *  要送給猜數字伺器的猜測
 *
 *  @param  x   使用者猜的數字
 */
case class Guess(x: String)

/**
 *  用這個訊息告訴猜數字 AI 開始猜數字
 */
case object StartGuess

/**
 *  猜數字的輔助函式
 */
trait GuessNumberFunctions
{
    // 產生猜數字遊戲裡所有可能答案的組合，共 5040 種
    //
    // 產生方式：
    //
    //  1. 先產生 0 到 9999 所有數字
    //  2. 將這些數字補零至四位數並轉成字串
    //  3. 過濾掉所有出現重覆數字的字串
    //
    def generateAllPossible = {

        //  重覆的定義：
        //
        //  將字串轉成字元集合後，如果大小不為四，就是有重覆的字元
        //
        //  例： "1234" -> Set(1, 2, 3, 4) -> 不重覆 (return false)
        //       "1224" -> Set(1, 2, 4)    -> 有重覆 (return true)
        def hasRepeatNumber (x: String) = x.toSet.size != 4
        def convertToPaddingString(x: Int) = "%04d" format (x)

        (0 to 9999).                     // 先產生 0 到 9999 的所有整數
            map(convertToPaddingString). // 將這些整數全轉成補零成四位數的字串
            filterNot(hasRepeatNumber)   // 濾出所有不含重覆數字的部份
    }

    /**
     *  比對兩個猜數字字串後，回覆幾 A 幾 B
     *
     *  詳細的說明請參考[前一篇文章][1]的講解。
     *
     *  [1]: http://bone.twbbs.org.tw/blog/archives/1787
     *
     *  @param  number1 第一個數字
     *  @param  number2 第二個數字
     */
    def compare(number1: String, number2: String) = {
    
        def pairIsSame(x: (Char, Char)) = x._1 == x._2

        val sumA = (number1 zip number2) filter (pairIsSame)

        val (remainNumber1, remainNumber2) =
            (number1 zip number2).
            filterNot (sumA contains _).
            unzip

        val sumB = remainNumber1 intersect remainNumber2
    
        (sumA.length, sumB.length)
    }
}

/**
 *  猜數字伺服器
 *
 *  這裡使用 Scala 的 Actor 來模擬老俞的作業裡的猜數字伺服器，
 *  而會使用這個方法的原因有兩個：
 *
 *    1. 練習 Scala 的 Actor 的概念和寫法
 *    2. 用這種方式實作比較簡單，不用處理 Socket
 *
 *  這個伺服器啟動後會做下面的事情：
 *
 *    1. 亂數產生一個猜數字正確解答
 *    2. 持續監聽別人丟過來的訊息
 *        - 當這個訊息是一個猜數字的猜測訊息時（Guess）物件，
 *          伺服器就會將其與正確答案比對，並回傳給猜數字客戶端
 *
 *        - 如果是其他訊息就略過並印出
 *
 *  這個伺服器會在以下情況結束：
 *
 *    - 客戶端猜中了正確數字
 *
 */
class GuessNumberServer extends Actor with GuessNumberFunctions
{
    // 亂數選擇正確答案
    val answer =
        Random.shuffle((0 to 9).toList) // 產生一個 List(0, 1, 2..., 9) 然後打亂之後
              .take(4)                  // 取前四個數字，例：List(3,4,1,5)
              .mkString                 // 再結合成字串，例："3415"

    // 印出正確答案
    println ("[debug] answer:" + answer)

    // 持續 (loop) 地監聽事件 (react)
    def act () = loop {
        react {

            // 當進來的是一個猜數字的訊息（可以把他當做是封包之類的）
            case Guess(x)  => 

                // 將傳進來的猜測與正確答比對
                val (statusA, statusB) = compare (answer, x)

                // 印出比對後是『幾 A 幾 B』
                println ("%s => %dA%dB" format(x, statusA, statusB))

                // 回傳給對方此次猜測的結果
                // x       => 這次他猜的數字
                // statusA => 數字對，位置也對的數量
                // statusB => 數字對，位置錯的數量
                sender ! Status(x, statusA, statusB)
                
                // 當 4A 時遊戲結束
                if (statusA == 4) Actor.exit()
            
            // 收到其他我不懂的訊息
            case otherwise =>
                println ("Server 收到不明訊息：" + otherwise)
        }
    }
}

/**
 *  猜數字 AI
 *
 *  猜數字的 AI 其實很簡單，但套句老俞的說法：『不要用人腦的思維來想這
 *  個問題！電腦和人腦的優點是不一樣的！』
 *
 *  記得：
 *   1. 電腦的處理速度很快
 *   2. 電腦的記憶量非常大
 *
 *  所以從電腦的角度來想，你可以用消去法來解猜數字的問題：
 *
 *   1. 把所有可能的解列出（一開始會有 5040 個）
 *   2. 當你猜一個數字，對方告訴你是 2A0B 的時候
 *   3. 把你所有可能解裡，和你猜的數字比對為 2A0B 的部份列出（假設此時已縮減到 1000 個可能解）
 *   4. 因為正解和你的解比對是 2A0B，所以正解一定在這 1000 個可能解裡
 *   5. 從這 1000 個可能解中，隨便找一個猜
 *   6. 4A 的話結束，不然的話依照 2 ~ 5 的方式繼續進行
 *
 *  這個 AI Client 做的事情也一模一樣，他可以處理以下的訊息：
 *
 *   - StartGame 
 *       開始向 Server 丟一個數字
 *
 *   - Status(myGuess, 4, 0)
 *       4A，所有數字和位置都對，遊戲結束
 *
 *   - Status(myGuess, a, b)
 *       過濾出可能解裡，和 myGuess 比對後為 aAbB 的解答之後繼續猜
 *
 *  @param  server  要連線到哪個 Server 猜數字
 */
class GuessNumberClient(server: GuessNumberServer) extends Actor with GuessNumberFunctions
{
    // 目前的可能解，一開始是全部的數字
    var possibleAnswer = generateAllPossible

    // 檢測 number1 和 number2 的結果是不是 nAnB
    def isSameStatus (number1: String, number2: String, nA: Int, nB: Int) = {
        compare(number1, number2) == (nA, nB)
    }

    // 持續 (loop) 地監聽 (react) 事件
    def act () = loop {
        react {

            // 如果開始玩猜數字遊戲
            case StartGuess => 

                // 從第一個數字猜
                println ("開始猜數字")
                server ! Guess(possibleAnswer.head)

            // 如果伺服器告訴我們猜中了，就印出答案並結束
            case Status(myGuess, 4, 0) => 
                println ("答案是：" + myGuess)
                Actor.exit()

            // 如果沒中的話，利用消去法，濾出剩下的所有組合理，
            // 同樣也是 nAnB 的部份
            case Status(myGuess, a, b) =>
                // 濾出剩下的可能答案
                possibleAnswer = possibleAnswer.filter(isSameStatus(_, myGuess, a, b))
                println ("現在的可能答案數：" + possibleAnswer.length)

                // 拿剩下的答案中的第一個繼續猜
                server ! Guess(possibleAnswer.head)
            
            // 如果收到不明訊息
            case otherwise =>
                println ("Client 收到不明訊息：" + otherwise)

        }
    }
}

object Main
{

    def main (args: Array[String]) {
        val server = new GuessNumberServer
        val client = new GuessNumberClient(server)

        server.start()  // 啟動 Server
        client.start()  // 啟動 AI Client

        client ! StartGuess // 開始玩猜數字！
    }
}