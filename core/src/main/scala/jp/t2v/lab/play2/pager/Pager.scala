package jp.t2v.lab.play2.pager

case class Pager[A](page: Int, size: Int, primarySorter: Sorter[A], optionalSorters: Sorter[A]*) {

  require(page > 0)
  require(size > 0)

  lazy val allSorters: Seq[Sorter[A]] = primarySorter +: optionalSorters

  lazy val limit: Int = size
  lazy val offset: Int = size * (page - 1)

}
