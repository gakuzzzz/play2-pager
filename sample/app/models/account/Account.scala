package models.account

import scalikejdbc._
import java.time.{LocalDateTime, LocalDate}
import jp.t2v.lab.play2.pager.{OrderType, Sortable}
import scalikejdbc.WrappedResultSet

case class Account(id: Int, name: Name, email: EMail, birthday: LocalDate, createdAt: LocalDateTime)

object Account extends SQLSyntaxSupport[Account] {
  override lazy val columns = autoColumns[Account]()

  def apply(s: SyntaxProvider[Account])(rs: WrappedResultSet): Account = autoConstruct(rs, s)

  implicit object sortable extends Sortable[Account] {
    def default: (String, OrderType) = ("id", OrderType.Descending)
    def acceptableKeys: Set[String] = Set("id", "name", "email", "birthday", "createdAt")
  }

}
