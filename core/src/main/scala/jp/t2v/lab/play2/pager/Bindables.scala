package jp.t2v.lab.play2.pager

import play.api.mvc.QueryStringBindable
import play.api.mvc.QueryStringBindable._

object Bindables {

  implicit def bindablePager[A](implicit ev: Sortable[A]) = new QueryStringBindable[Pager[A]] {
    def bind(_key: String, params: Map[String, Seq[String]]) = {
      val sorters = for {
        keys <- params.get("key")
        dirs <- params.get("dir")
      } yield keys zip dirs.flatMap(OrderType.valueOf) map {
        case (k, d) => ev.valueOf(k, d)
      }
      val (primary, optional) = sorters match {
        case Some(h +: t) => (h, t)
        case _            => (ev.defaultSorter, Nil)
      }
      Some(Right(Pager[A](
        page = bindableInt.bind("page", params).flatMap(_.right.toOption).getOrElse(1) max 1,
        size = bindableInt.bind("size", params).flatMap(_.right.toOption).getOrElse(ev.defaultPageSize) min ev.maxPageSize max 1,
        primarySorter = primary,
        optional: _*
      )))
    }
    def unbind(key: String, value: Pager[A]) =
      s"page=${value.page}&size=${value.size}&key=${value.allSorters.map(_.key).mkString("&key=")}&dir=${value.allSorters.map(_.dir.value).mkString("&dir=")}"
  }

}
