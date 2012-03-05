object Fun{
def ask = 2
def say(name:String) = println(name)

def run {
	 for(var start  = 'a' ;start < 'z';start = start+1)
	 	this say  start.toChar.toString
	}
	def main(args:Array[String]){
		run
	}
}