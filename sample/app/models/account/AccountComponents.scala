package models.account

trait AccountComponents {

  implicit private[account] lazy val accountDao: AccountDao = new AccountDao

  implicit lazy val accountService: AccountService = new AccountService

}
