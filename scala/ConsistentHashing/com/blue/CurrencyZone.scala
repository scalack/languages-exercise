package com.blue

abstract class CurrencyZone {
    //货币类型，虚类型，用于方法的参数化，由于上界为AbstractCurrency所以子类型中定义
    //的实类型必须是AbstractCurrency的子类
    type Currency <: AbstractCurrency
    //工厂方法，虚方法，创建货币对象
    def make(x: Long): Currency
    //货币类型基类
    abstract class AbstractCurrency {
        //数量
        val amount: Long
        //货币名称
        def designation: String
        //加法操作
        def + (that: Currency): Currency =
            make(this.amount + that.amount)
         //乘法操作
        def * (x: Double): Currency =
            make((this.amount * x).toLong)
        //减法操作
        def - (that: Currency): Currency =
            make(this.amount - that.amount)
        //除法操作
        def / (that: Double) =
            make((this.amount / that).toLong)
        //除法操作，（货币对象之间除）
        def / (that: Currency) =
            this.amount.toDouble / that.amount
        //汇率转换操作
        def from(other: CurrencyZone#AbstractCurrency): Currency =
            //从Converter.exchangeRate中取得汇率然后计算转换结果
            make(Math.round(
                other.amount.toDouble * Converter.exchangeRate
                    (other.designation)(this.designation)))
        //求以10为底的对数
        private def decimals(n: Long): Int =
            if (n == 1) 0 else 1 + decimals(n / 10)
        //货币的文本描述
        override def toString =
            //用货币的单位值来计算小数和整数部分
            ((amount.toDouble / CurrencyUnit.amount.toDouble)
            formatted ("%."+ decimals(CurrencyUnit.amount) +"f")
            +" "+ designation)
    }
    //货币单位值
    val CurrencyUnit: Currency
}
//汇率转换单例对象
object Converter {
   //货币汇率对照表
   var exchangeRate = Map(  
        "USD" -> Map("USD" -> 1.0 , "EUR" -> 0.7596,
                    "JPY" -> 1.211 , "CHF" -> 1.223),
        "EUR" -> Map("USD" -> 1.316 , "EUR" -> 1.0 ,
                    "JPY" -> 1.594 , "CHF" -> 1.623),
        "JPY" -> Map("USD" -> 0.8257, "EUR" -> 0.6272,
                    "JPY" -> 1.0 , "CHF" -> 1.018),
        "CHF" -> Map("USD" -> 0.8108, "EUR" -> 0.6160,
                    "JPY" -> 0.982 , "CHF" -> 1.0 )
    )
}
//美元区域单例对象
object US extends CurrencyZone {
    //美元基类
    abstract class Dollar extends AbstractCurrency {
        def designation = "USD"
    }
    //货币类型，父类中虚类型的实例化
    type Currency = Dollar
    //创建美元对象，cents为美分数
    def make(cents: Long) = new Dollar {
        val amount = cents
    }
    //1美分的值
    val Cent = make(1)
    //1美元的值
    val Dollar = make(100)
    //单位值为美元
    val CurrencyUnit = Dollar
}
//欧元区域单例对象
object Europe extends CurrencyZone {
    //欧元基类
    abstract class Euro extends AbstractCurrency {
        def designation = "EUR"
    }
    //货币类型
    type Currency = Euro
    //创建欧元对象，cents为欧分数
    def make(cents: Long) = new Euro {
        val amount = cents
    }
    //1欧分的值
    val Cent = make(1)
    //1欧元的值
    val Euro = make(100)
    //单位值为欧元
    val CurrencyUnit = Euro
}
//日元区域单例对象
object Japan extends CurrencyZone {
    //日元基类
    abstract class Yen extends AbstractCurrency {
        def designation = "JPY"
    }
    //货币类型
    type Currency = Yen
    //创建日元对象，yen为日元数
    def make(yen: Long) = new Yen {
        val amount = yen
    }
    //1日元的值
    val Yen = make(1)
    //单位值为日元
    val CurrencyUnit = Yen
}
//文件主单例对象，作为本文件的主执行程序
object App extends Application {
    //将100美元换成日元
    val yen : Japan.Currency = Japan.Yen from US.Dollar * 100
    println(yen)
    //将上述日元转换为欧元
    val euro = Europe.Euro from yen
    println(euro)
    //将上述欧元转换回美元
    val dollar = US.Dollar from euro
    println(dollar)
    //进行加法计算
    val d2 = US.Dollar * 100 + dollar
    println(d2)
}