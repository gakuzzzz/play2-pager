Play2-Pager [![Build Status](https://travis-ci.org/gakuzzzz/play2-pager.svg)](https://travis-ci.org/gakuzzzz/play2-pager)
=================================

Pager support for Play2 application.

![sample app screenshot](doc/img/play2-pager-sample-ss.png?raw=true)


## Target

Scala 2.11.x & 2.12.x & 2.13.x  
Play 2.5.x & 2.6.x & 2.7.x

## Setup

for Play 2.7.x

```scala
libraryDependencies += "jp.t2v" %% "play2-pager"             % "0.3.0"
libraryDependencies += "jp.t2v" %% "play2-pager-scalikejdbc" % "0.3.0" // optional. it is useful when you use scalikejdbc.
```

for Play 2.5.x, 2.6.x

```scala
libraryDependencies += "jp.t2v" %% "play2-pager"             % "0.2.0"
libraryDependencies += "jp.t2v" %% "play2-pager-scalikejdbc" % "0.2.0" // optional. it is useful when you use scalikejdbc.
```

## How to use

1. add imports into `templateImports` and `routesImport` in your Play Project `.sbt`.

    ```scala
    lazy val sample = (project in file("sample")).
      enablePlugins(PlayScala).
      settings(
        // ...snip
        templateImports ++= Seq(
          "jp.t2v.lab.play2.pager._"
        ),
        routesImport ++= Seq(
          "jp.t2v.lab.play2.pager.Pager",
          "jp.t2v.lab.play2.pager.Bindables._"
        ),
      )
    ```

1. define implicit `Sortable` value of your entities.

    * `default` is default sorting key and direction.
    * `acceptableKeys` is sortable keys of the entity.

    ```scala
    package models.account
    
    import scalikejdbc._
    import java.time.{LocalDateTime, LocalDate}
    import jp.t2v.lab.play2.pager.{OrderType, Sortable}
    
    case class Account(id: Int, name: Name, email: EMail, birthday: LocalDate, createdAt: LocalDateTime)
    
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
1. sbt sample/run
1. browse `http://localhost:9000/`
1. Click `Apply this script now!`


## License

This library is released under the Apache Software License, version 2,
which should be included with the source in a file named `LICENSE`.
