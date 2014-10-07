package jp.t2v.lab.play2.pager

case class Sorter[A] private[pager] (key: String, dir: OrderType) {
  def reverse: Sorter[A] = Sorter[A](key, dir.reverse)
}