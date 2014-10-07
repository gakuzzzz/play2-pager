package models.account

import jp.t2v.lab.play2.pager.{SearchResult, Pager}

class AccountService(implicit accountDao: AccountDao) {

  def findAll(pager: Pager[Account]): SearchResult[Account] = {
    SearchResult(pager, accountDao.countAll()) { pager =>
      accountDao.findAll(pager.allSorters, pager.limit, pager.offset)
    }
  }

}
