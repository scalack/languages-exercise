object Pen{
def maxListImpParm[T](elements: List[T])
	(implicit orderer: T => Ordered[T]): T =
	elements match {
			case List() =>
				throw new IllegalArgumentException("empty list!")
			case List(x) => x
			case x :: rest =>
							val maxRest = maxListImpParm(rest)(orderer)
				if (orderer(x) > maxRest) x
						else maxRest
}
def evenElems[T: ClassManifest](xs: Vector[T]): Array[T] = {
val arr = new Array[T]((xs.length + 1) / 2)
for (i <- 0 until xs.length by 2)
	arr(i / 2) = xs(i)
	arr
}
 def main(args:Array[String]){
  	maxListImpParm(List(1,2))
  	evenElems(Vector(1,2))
 }

}

