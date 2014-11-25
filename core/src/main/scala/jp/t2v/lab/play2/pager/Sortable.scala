package jp.t2v.lab.play2.pager

import annotation.implicitNotFound

@implicitNotFound("Sortable[${A}] could not be found.")
trait Sortable[A] {

  def acceptableKeys: Set[String]

  def default: (String, OrderType)

  final def defaultSorter: Sorter[A] = {
    val (k, d) = default
    Sorter(k, d)
  }

  def optionalDefaultSorters: Seq[Sorter[A]] = Nil

  def valueOf(key: String, dir: OrderType): Sorter[A] = {
    val (k, d) = acceptableKeys.find(_ == key).map((_, dir)) getOrElse default
    Sorter(k, d)
  }

  def defaultPageSize: Int = 30

  def maxPageSize: Int = 500

}