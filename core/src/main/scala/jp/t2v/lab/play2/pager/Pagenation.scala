package jp.t2v.lab.play2.pager

import play.api.mvc.Call
import play.twirl.api.Html

object pagenation {

  def apply[A](result: SearchResult[A],
               linkTo: Pager[A] => Call,
               window: Int = 4,
               showTruncate: Boolean = true,
               firstLabel: Html = Html("&laquo;"),
               previousLabel: Html = Html("&lsaquo;"),
               truncateLabel: Html = Html("..."),
               nextLabel: Html = Html("&rsaquo;"),
               lastLabel: Html = Html("&raquo;")): Html = {
    val calls = Calls(
      linkTo(result.firstPager),
      result.previousPager.map(linkTo),
      result.nextPager.map(linkTo),
      linkTo(result.lastPager),
      result.pagerWindow(window).map(p => p.page -> linkTo(p))
    )
    val previousTruncate = showTruncate && result.minPage < result.pager.page - window
    val nextTruncate = showTruncate && result.maxPage > result.pager.page + window
    html.pagenation(result, calls, previousTruncate, nextTruncate, firstLabel, previousLabel, truncateLabel, nextLabel, lastLabel)
  }

}
