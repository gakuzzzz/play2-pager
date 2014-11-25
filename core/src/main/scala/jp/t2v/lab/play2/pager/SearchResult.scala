package jp.t2v.lab.play2.pager

import scala.concurrent.{ExecutionContext, Future}


case class SearchResult[A](pager: Pager[A], items: Seq[A], totalCount: Long) {

  lazy val minPage: Int = 1
  lazy val maxPage: Int = Math.ceil(totalCount.toDouble / pager.size.toDouble).toInt max 1

  lazy val hasPrevious: Boolean = pager.page > minPage
  lazy val hasNext: Boolean = pager.page < maxPage

  lazy val firstPager: Pager[A] = move(minPage)
  lazy val previousPager: Option[Pager[A]] = if (hasPrevious) Some(move(pager.page - 1)) else None
  lazy val nextPager: Option[Pager[A]] = if (hasNext) Some(move(pager.page + 1)) else None
  lazy val lastPager: Pager[A] = move(maxPage)

  def window(size: Int): Seq[Int] = ((pager.page - size) max minPage) to ((pager.page + size) min maxPage)
  def pagerWindow(size: Int): Seq[Pager[A]] = window(size).map(move)

  def keyPager(key: String, optionalSorterNum: Int = 0)(implicit ev: Sortable[A]): Pager[A] = {
    val (current, others) = pager.allSorters.partition(_.key == key)
    val primary = current.headOption.map(_.reverse) getOrElse ev.valueOf(key, OrderType.Ascending)
    val optional = if (optionalSorterNum < 0) others else others.take(optionalSorterNum)
    Pager[A](minPage, pager.size, primary, optional: _*)
  }

  def keyOrder(key: String): Option[OrderType] = pager.keyOrder(key)

  private[this] def move(page: Int): Pager[A] = Pager[A](page, pager.size, pager.primarySorter, pager.optionalSorters: _*)

}
object SearchResult {

  def apply[A](pager: Pager[A], count: => Long)(finder: Pager[A] => Seq[A]): SearchResult[A] = {
    SearchResult(pager, finder(pager), count)
  }

  def apply[A](pager: Pager[A], count: => Future[Long])(finder: Pager[A] => Future[Seq[A]])(implicit ctx: ExecutionContext): Future[SearchResult[A]] = {
    finder(pager).zip(count).map {
      case (items, c) =>  SearchResult(pager, items, c)
    }
  }

}