package jp.t2v.lab.play2.pager

import _root_.scalikejdbc._

package object scalikejdbc {

  import OrderType._

  implicit class SorterWrapper[A](val value: Sorter[A]) extends AnyVal {

    def toSQLSyntax(s: SyntaxProvider[A]): SQLSyntax = value.dir match {
      case Ascending  => s.field(value.key).asc
      case Descending => s.field(value.key).desc
    }

  }

}
