val _version = "1.0-SNAPSHOT"

val _crossScalaVersions = Seq("2.11.2", "2.10.4")

val _org = "jp.t2v"

lazy val root = (project in file(".")).
  aggregate(core, scalikejdbc, sample).
  settings(
    aggregate in update := false
  )

lazy val core = (project in file("core")).
  settings(
    organization := _org,
    name := "play2-pager",
    version := _version,
    crossScalaVersions := _crossScalaVersions,
    libraryDependencies ++= Seq(
      "com.typesafe.play"  %%   "play"   %  "2.3.4"
    )
  )

lazy val scalikejdbc = (project in file("scalikejdbc")).
  settings(
    organization := _org,
    name := "play2-pager-scalikejdbc",
    version := _version,
    crossScalaVersions := _crossScalaVersions,
    libraryDependencies ++= Seq(
      "org.scalikejdbc"  %% "scalikejdbc"  % "2.1.+"
    )
  )

lazy val sample = (project in file("sample")).
  dependsOn(core, scalikejdbc).
  enablePlugins(PlayScala).
  settings(
    crossScalaVersions := _crossScalaVersions,
    libraryDependencies ++= Seq(
      "com.h2database"  %  "h2"                 % "1.4.+",
      "ch.qos.logback"  %  "logback-classic"    % "1.1.+"
    ),
    play.PlayImport.PlayKeys.routesImport ++= Seq(
      "jp.t2v.lab.play2.pager.Bindables._"
    ),
    TwirlKeys.templateImports ++= Seq(
      "jp.t2v.lab.play2.pager._"
    )
  )
