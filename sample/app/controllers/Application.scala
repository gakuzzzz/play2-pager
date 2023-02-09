package controllers

import javax.inject._

import play.api.mvc._
import models.ComponentRegistry
import jp.t2v.lab.play2.pager.Pager
import models.account.Account

@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) with ComponentRegistry {

  def index(pager: Pager[Account]) = Action {
    Ok(views.html.index(accountService.findAll(pager)))
  }

}