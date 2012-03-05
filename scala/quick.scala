def qsort[T <% Ordered[T]](list:List[T]):List[T] = {
  list match {
    case Nil         => Nil
    case pivot::tail => qsort(tail.filter(_ < pivot)) ::: pivot :: qsort(tail.filter(_ >= pivot))
  }
}
def te = {
}
