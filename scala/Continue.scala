import util.continuations._
object Continue{
	def say = 
	reset {
		shift {
			cf:(Int=>Int) =>
			 val even = cf(10)
			 println(even)
			 val one = cf(100)
			 println(one)
			 one
			
	}	+1
 }
 
 def main(args:Array[String]){
    say	
 }
}