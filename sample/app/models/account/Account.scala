package models.account

import scalikejdbc._
import org.joda.time.{DateTime, LocalDate}
import jp.t2v.lab.play2.pager.{OrderType, Sortable}
import scalikejdbc.WrappedResultSet

case class Account(id: Int, name: Name, email: EMail, birthday: LocalDate, createdAt: DateTime)

object Account extends SQLSyntaxSupport[Account] {

  def apply(s: SyntaxProvider[Account])(rs: WrappedResultSet): Account = autoConstruct(rs, s)

  implicit object sortable extends Sortable[Account] {
    def default: (String, OrderType) = ("id", OrderType.Descending)
    def acceptableKeys: Set[String] = Set("id", "name", "email", "birthday", "createdAt")
  }

}
