package jp.t2v.lab.play2.pager

import play.api.mvc.Call

private[pager] case class Calls(first: Call, previous: Option[Call], next: Option[Call], last: Call, window: Seq[(Int, Call)])
