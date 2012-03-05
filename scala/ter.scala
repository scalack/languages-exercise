object TernaryOp {
  class Ternary[T](t: T) {
    println("Ternary") 
    def is[R](bte: BranchThenElse[T,R]) = if (bte.branch(t)) bte.then(t) else bte.elze(t)
  }
  class Branch[T](branch: T => Boolean) {
    println("branch");
    def ?[R] (then: T => R) = new BranchThen(branch,then)
  }
  class BranchThen[T,R](val branch: T => Boolean, val then: T => R){
     println("BranchThen")
  }
  
  class Elze[T,R](elze: T => R) {
     println("Elze")
    def :: (bt: BranchThen[T,R]) = new BranchThenElse(bt.branch,bt.then,elze)
  }
  class BranchThenElse[T,R](val branch: T => Boolean, val then: T => R, val elze: T => R)
  
  implicit def any2Ternary[T](t: T) = new Ternary(t)
  implicit def fct2Branch[T](branch: T => Boolean) = new Branch(branch)
  implicit def fct2Elze[T,R](elze: T => R) = new Elze(elze)
  
  def test = {
  this.getClass.getSimpleName is {s: String => s.endsWith("$")} ? {s: String => s.init} :: {s: String => s}
}
}