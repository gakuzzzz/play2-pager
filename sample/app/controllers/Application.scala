package controllers

import play.api.mvc._
import models.ComponentRegistry
import jp.t2v.lab.play2.pager.Pager
import models.account.Account

trait Application extends Controller with ComponentRegistry {

  def index(pager: Pager[Account]) = Action {
    Ok(views.html.index(accountService.findAll(pager)))
  }

}

object Application extends Application