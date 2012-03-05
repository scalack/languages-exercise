case class Address(no: Int, street: String, city: String, state: String, zip: String)

trait LabelMaker[T] {

    def toLabel(value: T): String

}

trait te{
	 implicit object StringLabelMaker extends LabelMaker[String] {

    def toLabel(s: String) = s

  }
}
  
  
object LabelMaker {

 


  implicit object AddressLabelMaker extends LabelMaker[Address] {

    def toLabel(a: Address) = {

      import a._

      "%d %s, %s, %s - %s".format(no, street, city, state, zip)  

    }
  }  
  /*
  // If this is not commented, the compiler complains about ambiguity

  implicit object AnotherStringLabelMaker extends LabelMaker[String] {
    def toLabel(s: String) = s.toUpperCase

  }
  */
 implicit def toMaker[T](a:T):LabelMaker[T] = 
  new LabelMaker[T]{def toLabel(value: T) = "hello"}
  
 

}

object Main {
   
  //query implicit˳��Ϊ��
  //1. ��ǰcontext scope
  //2. LabelMaker�ڲ�����ģ��Ͱ��������ڶ����implicit
  //3. LabelMaker ��������implicit object
  def makeLabel[T](t: T)(implicit lm :LabelMaker[T]) = lm toLabel t 
  
  // ������LabelMaker��toMaker ���� implicit
  def makeLabel[T](t: T)(implicit lm :T => LabelMaker[T]) = t toLabel t


  def main(args:Array[String]) {

    val address = Address(42, "Monroe Street", "Denver", "CO", "80123")
 
    println(makeLabel(address))

    println(makeLabel("a String"))


   
  }

}

 