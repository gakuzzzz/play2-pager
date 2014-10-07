package models.account

import scalikejdbc._
import jp.t2v.lab.play2.pager.Sorter
import jp.t2v.lab.play2.pager.scalikejdbc._

private[account] class AccountDao {

  def findAll(orders: Seq[Sorter[Account]], limit: Int, offset: Int)(implicit session: DBSession = AutoSession): Seq[Account] = {
    val a = Account.syntax("a")
    withSQL {
      select.from(Account as a).orderBy(orders.map(_.toSQLSyntax(a)): _*).limit(limit).offset(offset)
    }.map(Account(a)).list().apply()
  }

  def countAll()(implicit session: DBSession = AutoSession): Long = {
    val a = Account.syntax("a")
    withSQL {
      select(sqls.count).from(Account as a)
    }.map(_.long(1)).single().apply() getOrElse 0
  }

}
