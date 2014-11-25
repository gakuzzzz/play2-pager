Play2-Pager [![Build Status](https://travis-ci.org/gakuzzzz/play2-pager.svg)](https://travis-ci.org/gakuzzzz/play2-pager)
=================================

Pager support for Play2 application.

![sample app screenshot](doc/img/play2-pager-sample-ss.png?raw=true)



## Setup

    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

    libraryDependencies += "jp.t2v" %% "play2-pager"             % "0.1.0-SNAPSHOT"
    libraryDependencies += "jp.t2v" %% "play2-pager-scalikejdbc" % "0.1.0-SNAPSHOT" // optional. it is useful when you use scalikejdbc.

## How to use

1. add imports into `templateImports` and `routesImport`

    ```scala
    TwirlKeys.templateImports += "jp.t2v.lab.play2.pager._"
    play.PlayImport.PlayKeys.routesImport += "jp.t2v.lab.play2.pager.Pager"
    play.PlayImport.PlayKeys.routesImport += "jp.t2v.lab.play2.pager.Bindables._"
    ```

1. define implicit `Sortable` value of your entities.

    * `default` is default sorting key and direction.
    * `acceptableKeys` is sortable keys of the entity.

    ```scala
package models.account

import scalikejdbc._
import org.joda.time.{DateTime, LocalDate}
import jp.t2v.lab.play2.pager.{OrderType, Sortable}

case class Account(id: Int, name: Name, email: EMail, birthday: LocalDate, createdAt: DateTime)

object Account extends SQLSyntaxSupport[Account] {

  def apply(s: SyntaxProvider[Account])(rs: WrappedResultSet): Account = autoConstruct(rs, s)

  implicit object sortable extends Sortable[Account] {
    val default: (String, OrderType) = ("id", OrderType.Descending)
    val acceptableKeys: Set[String] = Set("id", "name", "email", "birthday", "createdAt")
  }

}
    ```

1. define controller that receive a `Pager` of your entity.

    ```scala
  def index(pager: Pager[Account]) = Action {
    Ok(views.html.index(accountService.findAll(pager)))
  }
    ```

1. define service that returns a `SearchResult` of your entity.

    ```scala
package models.account

import jp.t2v.lab.play2.pager.{SearchResult, Pager}

class AccountService(implicit accountDao: AccountDao) {

  def findAll(pager: Pager[Account]): SearchResult[Account] = {
    SearchResult(pager, accountDao.countAll()) { pager =>
      accountDao.findAll(pager.allSorters, pager.limit, pager.offset)
    }
  }

}
    ```

1. define template that receive a `SearchResult` of your entity.

    You can use `@pagination` to render pagination.

    ```scala
@(result: SearchResult[Account])

@main("Welcome to Play2 Pager") {

    ...snip

    @pagination(result, routes.Application.index)

}
    ```

    More details, see [Sample App](sample)

## Sample App

1. git clone
1. cd play2-pager
1. activator "project sample" run
1. browse `http://localhost:9000/`
1. Click `Apply this script now!`


## License

This library is released under the Apache Software License, version 2,
which should be included with the source in a file named `LICENSE`.
