package jp.t2v.lab.play2.pager

sealed abstract class OrderType(val value: String) {
  import OrderType._
  lazy val reverse: OrderType = this match {
    case Ascending  => Descending
    case Descending => Ascending
  }
}
object OrderType {

  case object Ascending extends OrderType("asc")
  case object Descending extends OrderType("desc")

  lazy val values: Seq[OrderType] = Vector(Ascending, Descending)

  def valueOf(value: String): Option[OrderType] = values.find(_.value == value)

}
